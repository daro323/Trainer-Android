package com.trainer.ui.training.cyclic

import android.os.Bundle
import android.view.View
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.base.OnBackSupportingFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.model.ProgressStatus
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.ioMain
import com.trainer.modules.countdown.CountDownState
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
  private val presenterHelper: CyclicPresenterHelper by lazy { (trainingManager.workoutPresenter?.getHelper() ?: throw IllegalStateException("Current workout presenter not set!") ) as CyclicPresenterHelper }  // can call this only after component.inject()!

  private val cycleViewModel: CycleViewModel = CycleViewModel.createNew()
  private val viewModelChengesProcessor = BehaviorProcessor.create<CycleViewModel>()

  private var workoutEventsSubscription = Disposables.disposed()
  private var timerDisposable = Disposables.disposed()
  private var performEventsDisposable = Disposables.disposed()

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    header.bindViewModel(this)
    body.bindViewModel(this)
    footer.bindViewModel(this)
    initViewModel()
  }

  override fun onStart() {
    super.onStart()
    subscribeForCycleStateEvents()
  }

  override fun onStop() {
    workoutEventsSubscription.dispose()
    timerDisposable.dispose()
    performEventsDisposable.dispose()
    super.onStop()
  }

  // Back click only available when cycle is not ongoing
  override fun onBackPressed() = when (cycleViewModel.state) {
    NEW, DONE, COMPLETE -> false
    else -> true
  }

  override fun getViewModelChanges() = viewModelChengesProcessor.toObservable()

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

  private fun subscribeForGetReadyCountDown() {
    timerDisposable.dispose()
    CountingDownTimer()
        .apply {
          timerDisposable = onCountDownEvents()
              .ioMain()
              .doOnSubscribe { cycleViewModel.state = GET_READY }
              .doOnDispose { abort() }
              .subscribe {
                cycleViewModel.bodyViewModel.countDown = it.countDown
                if (it.state == CountDownState.FINISHED) {
                  timerDisposable.dispose()
                  subscribeForPerformingEvents()
                }
              }
        }.start(CyclicPresenterHelper.GET_READY_TIME_SEC)
  }

  private fun subscribeForCycleStateEvents() {
    workoutEventsSubscription.dispose()
    workoutEventsSubscription = presenterHelper.getCycleStateEvents()
        .ioMain()
        .subscribe { handleCycleStateEvent(it) }
  }

  private fun subscribeForPerformingEvents() {
    val cycle = presenterHelper.getSerie()
    val currentRoutine = presenterHelper.getCurrentRoutine()

    cycleViewModel.state = PERFORMING
    cycleViewModel.bodyViewModel.countDown = currentRoutine.durationTimeSec
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
        .subscribe {
          cycleViewModel.bodyViewModel.countDown = it.countDown
          viewModelChengesProcessor.onNext(cycleViewModel)
          if (it.state == CountDownState.FINISHED) {
            performEventsDisposable.dispose()
            presenterHelper.onRoutineComplete()
          }
        }
  }

  private fun initViewModel() {
    val cycle = presenterHelper.getSerie()
    val currentRoutine = presenterHelper.getCurrentRoutine()

    // State
    when (cycle.status()) {
      ProgressStatus.NEW -> cycleViewModel.state = NEW
      ProgressStatus.COMPLETE -> cycleViewModel.state = NEW
      else -> {
      } // Ignore
    }

    // Header
    cycleViewModel.headerViewModel.apply {
      exerciseName = currentRoutine.exercise.name
      cycleCount = cycle.cyclesCount
      lastCycleCount = cycle.lastCyclesCount
    }

    cycleViewModel.footerViewModel.nextExerciseName = currentRoutine.exercise.name

    viewModelChengesProcessor.onNext(cycleViewModel)
  }

  private fun handleCycleStateEvent(state: CycleState) {
    // TODO
    when (state) {
      NEW -> {}

      GET_READY -> subscribeForGetReadyCountDown()

      PERFORMING -> { subscribeForPerformingEvents() }

      RESTING -> {} // between routines

      DONE -> {}

      COMPLETE -> {}
    }
  }
}