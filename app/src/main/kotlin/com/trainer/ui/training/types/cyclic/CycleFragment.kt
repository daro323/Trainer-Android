package com.trainer.ui.training.types.cyclic

import android.os.Bundle
import android.view.View
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.base.OnBackSupportingFragment
import com.trainer.modules.training.workout.WorkoutManager
import com.trainer.modules.training.workout.WorkoutPresenter
import com.trainer.modules.training.workout.WorkoutEvent
import com.trainer.extensions.ioMain
import com.trainer.extensions.start
import com.trainer.modules.countdown.CountingDownTimer
import com.trainer.modules.training.workout.types.cyclic.CycleState
import com.trainer.modules.training.workout.types.cyclic.CycleState.*
import com.trainer.modules.training.workout.types.cyclic.CyclicPresenterHelper
import com.trainer.ui.training.rest.RestActivity
import com.trainer.ui.training.types.cyclic.CycleViewEvent.*
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.fragment_cycle.*
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CycleFragment : BaseFragment(R.layout.fragment_cycle), OnBackSupportingFragment {
  @Inject lateinit var workoutManager: WorkoutManager
  private val presenter: WorkoutPresenter by lazy { workoutManager.workoutPresenter ?: throw IllegalStateException("Current workout presenter not set!") }  // can call this only after component.inject()!
  private val presenterHelper: CyclicPresenterHelper by lazy { presenter.getHelper() as CyclicPresenterHelper }  // can call this only after component.inject()!

  private lateinit var fragmentPresenter: CycleFragmentPresenter

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
    fragmentPresenter = CycleFragmentPresenter(handleViewEvent).apply {
      header.bindViewModel(this)
      body.bindViewModel(this)
      footer.bindViewModel(this)
    }
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
  override fun onBackPressed() = when (fragmentPresenter.getDisplayState()) {
    NEW, DONE, COMPLETE -> false
    else -> true
  }

  private fun subscribeForWorkoutEvents() {
    workoutEventsSubscription.dispose()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .ioMain()
        .filter { it == WorkoutEvent.REST }   // Rest between cycles - start full screen rest
        .subscribe {
          presenter.onStartRest()
          activity.start<RestActivity>()
        }
  }

  private fun subscribeForGetReadyCountDown() {
    timerDisposable.dispose()
    timerDisposable = CountingDownTimer().start(CyclicPresenterHelper.GET_READY_TIME_SEC)
        .ioMain()
        .subscribe {
          fragmentPresenter.displayGetReady(it, presenterHelper.getCurrentRoutine().exercise.name)
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

    performEventsDisposable.dispose()
    performEventsDisposable = presenterHelper.getPerformEvents()
        .ioMain()
        .filter { it >= 0 }
        .subscribe {
          fragmentPresenter.displayPerforming(
              performCountDown = it,
              cyclesCount = cycle.cyclesCount,
              lastCyclesCount = cycle.lastCyclesCount,
              currentRoutineNr = presenterHelper.getCurrentCycleNumber(),
              routinesCount = presenterHelper.getCycleRoutinesCount(),
              currentRoutine = currentRoutine,
              nextRoutine = presenterHelper.getNextRoutine())
          if (it == 0) {
            performEventsDisposable.dispose()
            presenterHelper.onCompleteRoutine()
          }
        }
  }

  private fun subscribeForRest() {
    val currentRoutine = presenterHelper.getCurrentRoutine()
    restDisposable.dispose()
    restDisposable = presenterHelper.getRestBetweenRoutinesEvents()
        .filter { it >= 0 }
        .ioMain()
        .doOnSubscribe {
          presenterHelper.onStartRestBetweenRoutines()
        }
        .doOnNext {
          fragmentPresenter.displayResting(
              restCountDown = it,
              restTotalTime = currentRoutine.restTimeSec,
              nextExerciseName = presenterHelper.getNextRoutine()!!.exercise.name)
        }
        .subscribe {
          if (it == 0) {
            presenterHelper.onRestedBetweenRoutines()
            restDisposable.dispose()
          }
        }
  }

  private fun initViewModel() {
    fragmentPresenter.displayNew(presenterHelper.getCurrentRoutine().exercise.name,
        presenterHelper.getSerie().lastCyclesCount,
        presenterHelper.isCycleCurrentSerie())
  }

  private val handleViewEvent = { event: CycleViewEvent ->
    when (event) {
      START -> presenterHelper.onStartCycle()

      ONE_MORE -> presenterHelper.onStartAnotherCycle()

      FINISH -> presenterHelper.onCompleteCycle()
    }
  }

  private fun handleCycleStateEvent(state: CycleState) {
    when (state) {
      NEW -> initViewModel()

      GET_READY -> subscribeForGetReadyCountDown()

      PERFORMING -> subscribeForPerformingEvents()

      RESTING -> subscribeForRest()

      DONE -> presenterHelper.getSerie().run { fragmentPresenter.displayDone(cyclesCount, lastCyclesCount) }

      COMPLETE -> presenterHelper.getSerie().run { fragmentPresenter.displayComplete(cyclesCount, lastCyclesCount) }
    }
  }
}