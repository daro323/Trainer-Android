package com.trainer.ui.training.cyclic

import android.os.Bundle
import android.view.View
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.base.OnBackSupportingFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.business.WorkoutPresenter
import com.trainer.core.training.model.WorkoutEvent
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.ioMain
import com.trainer.modules.countdown.CountingDownTimer
import com.trainer.modules.training.types.cyclic.CycleState
import com.trainer.modules.training.types.cyclic.CycleState.*
import com.trainer.modules.training.types.cyclic.CyclicPresenterHelper
import com.trainer.ui.training.cyclic.CycleViewEvent.*
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import kotlinx.android.synthetic.main.fragment_cycle.*
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CycleFragment : BaseFragment(R.layout.fragment_cycle), OnBackSupportingFragment, CycleViewCallback {
  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout presenter not set!") }  // can call this only after component.inject()!
  private val presenterHelper: CyclicPresenterHelper by lazy { presenter.getHelper() as CyclicPresenterHelper }  // can call this only after component.inject()!

  private val cycleViewModel: CycleViewModel = CycleViewModel.createNew()
  private val viewModelChengesProcessor = BehaviorProcessor.create<CycleViewModel>()

  private var workoutEventsSubscription = Disposables.disposed()
  private var cycleStateEventsSubscription = Disposables.disposed()
  private var timerDisposable = Disposables.disposed()
  private var performEventsDisposable = Disposables.disposed()
  private var restDisposable = Disposables.disposed()

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    header.bindViewModel(this)
    body.bindViewModel(this)
    footer.bindViewModel(this)
  }

  override fun onStart() {
    super.onStart()
    subscribeForWorkoutEvents()
    subscribeForCycleStateEvents()
  }

  override fun onStop() {
    workoutEventsSubscription.dispose()
    cycleStateEventsSubscription.dispose()
    timerDisposable.dispose()
    performEventsDisposable.dispose()
    restDisposable.dispose()
    super.onStop()
  }

  // Back click only available when cycle is not ongoing
  override fun onBackPressed() = when (cycleViewModel.state) {
    NEW, DONE, COMPLETE -> false
    else -> true
  }

  override fun getViewModelChanges() = viewModelChengesProcessor.toObservable()

  private fun subscribeForWorkoutEvents() {
    workoutEventsSubscription.dispose()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .ioMain()
        .filter { it == WorkoutEvent.REST }   // Rest between cycles - start full screen rest
        .subscribe {
          // TODO
        }
  }

  private fun subscribeForGetReadyCountDown() {
    timerDisposable.dispose()
    timerDisposable = CountingDownTimer().start(CyclicPresenterHelper.GET_READY_TIME_SEC)
        .ioMain()
        .doOnSubscribe { cycleViewModel.state = GET_READY }
        .subscribe {
          cycleViewModel.bodyViewModel.countDown = it
          viewModelChengesProcessor.onNext(cycleViewModel)
          if (it == 0) {
            timerDisposable.dispose()
            presenterHelper.onPrepared()
          }
        }
  }

  private fun subscribeForCycleStateEvents() {
    cycleStateEventsSubscription.dispose()
    cycleStateEventsSubscription = presenterHelper.getCycleStateEvents()
        .ioMain()
        .subscribe { handleCycleStateEvent(it) }
  }

  private fun subscribeForPerformingEvents() {
    val cycle = presenterHelper.getSerie()
    val currentRoutine = presenterHelper.getCurrentRoutine()

    cycleViewModel.state = PERFORMING
    cycleViewModel.bodyViewModel.apply {
      countDown = currentRoutine.durationTimeSec
      totalCountDown = currentRoutine.durationTimeSec
    }
    cycleViewModel.headerViewModel.apply {
      exerciseName = currentRoutine.exercise.name
      cycleCount = cycle.cyclesCount
      lastCycleCount = cycle.lastCyclesCount
    }
    cycleViewModel.footerViewModel.apply {
      presenterHelper.getNextRoutine()?.apply { nextExerciseName = exercise.name }
      currentCount = presenterHelper.getCurrentCycleNumber()
      totalCount = presenterHelper.getCycleRoutinesCount()
    }
    viewModelChengesProcessor.onNext(cycleViewModel)

    performEventsDisposable.dispose()
    performEventsDisposable = presenterHelper.getPerformEvents()
        .ioMain()
        .filter { it >= 0 }
        .subscribe {
          cycleViewModel.bodyViewModel.countDown = it
          viewModelChengesProcessor.onNext(cycleViewModel)
          if (it == 0) {
            performEventsDisposable.dispose()
            presenterHelper.onCompleteRoutine()
          }
        }
  }

  private fun subscribeForRest() {
    val currentRoutine = presenterHelper.getCurrentRoutine()

    cycleViewModel.state = RESTING
    cycleViewModel.bodyViewModel.apply {
      countDown = currentRoutine.restTimeSec
      totalCountDown = currentRoutine.restTimeSec
    }
    cycleViewModel.footerViewModel.nextExerciseName = presenterHelper.getNextRoutine()!!.exercise.name
    viewModelChengesProcessor.onNext(cycleViewModel)

    restDisposable.dispose()
    restDisposable = presenterHelper.getRestBetweenRoutinesEvents()
        .filter { it >= 0 }
        .ioMain()
        .doOnSubscribe {
          presenterHelper.onStartRestBetweenRoutines()
        }
        .doOnNext {
          cycleViewModel.bodyViewModel.countDown = it
          viewModelChengesProcessor.onNext(cycleViewModel)
        }
        .subscribe {
          if (it == 0) {
            presenterHelper.onRestedBetweenRoutines()
            restDisposable.dispose()
          }
        }
  }

  private fun initViewModel() {
    val cycle = presenterHelper.getSerie()
    val currentRoutine = presenterHelper.getCurrentRoutine()

    cycleViewModel.state = NEW

    cycleViewModel.headerViewModel.apply {
      exerciseName = currentRoutine.exercise.name
      cycleCount = cycle.cyclesCount
      lastCycleCount = cycle.lastCyclesCount
    }

    cycleViewModel.footerViewModel.nextExerciseName = currentRoutine.exercise.name

    viewModelChengesProcessor.onNext(cycleViewModel)
  }

  override fun onViewEvent(event: CycleViewEvent) {
    // TODO
    when (event) {
      START -> presenterHelper.onStartCycle()

      ONE_MORE -> {
      }

      FINISH -> {
      }
    }
  }

  private fun handleCycleStateEvent(state: CycleState) {
    // TODO
    when (state) {
      NEW -> initViewModel()

      GET_READY -> subscribeForGetReadyCountDown()

      PERFORMING -> subscribeForPerformingEvents()

      RESTING -> subscribeForRest()

      DONE -> {
      }

      COMPLETE -> {
      }
    }
  }
}