package com.trainer.modules.training.types.cyclic

import android.support.annotation.Nullable
import com.trainer.R
import com.trainer.core.training.business.WorkoutPresenterHelper
import com.trainer.core.training.model.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.core.training.model.ProgressStatus.*
import com.trainer.core.training.model.Serie
import com.trainer.modules.countdown.CountDownNotification
import com.trainer.modules.countdown.CountDownNotification.Companion.DUMMY_REQUESTCODE
import com.trainer.modules.training.rest.RestManager
import com.trainer.ui.training.SerieActivity
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CyclicPresenterHelper @Inject constructor(val performManager: PerformManager,
                                                val restManager: RestManager) : WorkoutPresenterHelper {
  private lateinit var cycle: Cycle
  private lateinit var callback: WorkoutPresenterHelper.HelperCallback
  private var currentRoutineIdx = VALUE_NOT_SET
  private val cycleStateEventsProcessor = BehaviorProcessor.create<CycleState>()

  companion object {
    const val DEFAULT_ROUTINE_INDEX = 0
    const val GET_READY_TIME_SEC = 3
  }

  override fun initWith(serie: Serie, callback: WorkoutPresenterHelper.HelperCallback) {
    require(serie is Cycle) { "CyclicPresenterHelper expects to be initialized with Cycle Serie type! (instead it was ${serie.javaClass.name})" }
    cycle = serie as Cycle
    this.callback = callback
    when (cycle.status()) {
      NEW -> {
        refreshCurrentRoutineIdx()
        cycleStateEventsProcessor.onNext(CycleState.NEW)
      }

      STARTED -> {
        refreshCurrentRoutineIdx()
        cycleStateEventsProcessor.onNext(CycleState.DONE)
      }

      COMPLETE -> {
        currentRoutineIdx = DEFAULT_ROUTINE_INDEX
        cycleStateEventsProcessor.onNext(CycleState.COMPLETE)
      }
    }
  }

  override fun getSerie() = cycle

  override fun determineNextStep() {
    if (isCurrentRoutineTheLast()) {
      cycleStateEventsProcessor.onNext(CycleState.DONE)
    } else {
      refreshCurrentRoutineIdx()
      startPerforming()
    }
  }

  override fun onSkipSerie() {
    if (restManager.isResting()) restManager.abortRest()
    if (performManager.isPerforming()) performManager.abortPerforming()
  }

  fun getCurrentRoutine() = cycle.cycleList[currentRoutineIdx]

  @Nullable
  fun getNextRoutine() = if (isCurrentRoutineTheLast()) null else cycle.cycleList[currentRoutineIdx + 1]

  fun getCycleRoutinesCount() = cycle.cycleList.size

  fun getCurrentCycleNumber() = cycle.cycleList.indexOf(getCurrentRoutine()) + 1

  fun onStartCycle() {
    require(currentRoutineIdx != VALUE_NOT_SET) { "Attempt to start cycle when current cycle routine is not set!" }
    cycle.start()
    cycleStateEventsProcessor.onNext(CycleState.GET_READY)
  }

  fun onStartAnotherCycle() {
    cycle.startNext()
    refreshCurrentRoutineIdx()
    cycleStateEventsProcessor.onNext(CycleState.GET_READY)
  }

  fun onCompleteRoutine() {
    getCurrentRoutine().isComplete = true
    performManager.onPerformingComplete()
    if (isCurrentRoutineTheLast()) {
      cycle.cyclesCount++
      callback.onSaveSerie(cycle)
      cycleStateEventsProcessor.onNext(CycleState.DONE)
    } else {
      cycleStateEventsProcessor.onNext(CycleState.RESTING)
    }
  }

  fun isCycleCurrentSerie() = !(cycle.status() == COMPLETE || callback.hasOtherSerieStarted(cycle))

  fun onCompleteCycle() {
    cycle.done()
    callback.onSaveSerie(cycle, true)
  }

  fun onPrepared() {
    require(currentRoutineIdx != VALUE_NOT_SET) { "Attempt to start cycle when current cycle routine is not set!" }
    startPerforming()
  }

  fun getPerformEvents() = performManager.getPerformEvents()

  override fun getRestTime() = cycle.restTimeSec

  fun onStartRestBetweenRoutines() {
    restManager.startRest(getCurrentRoutine().restTimeSec,
        notificationData = CountDownNotification.InitData(R.string.rest_in_progress_notification_title, SerieActivity::class.java.name, DUMMY_REQUESTCODE, CountDownNotification.CYCLE_ROUTINE_REST_NOTIFICATION_ID),
        withVibration = false)
  }

  fun onRestedBetweenRoutines() {
    restManager.onRestComplete()
    determineNextStep()
  }

  fun getRestBetweenRoutinesEvents() = restManager.getRestEvents()

  fun getCycleStateEvents() = cycleStateEventsProcessor.toObservable()

  private fun isCurrentRoutineTheLast() = cycle.cycleList.last() == getCurrentRoutine()

  /**
   * Updates the currentRoutineIdx to the first not completed routine
   */
  private fun refreshCurrentRoutineIdx() {
    require(cycle.status() != COMPLETE) { "Can't refreshCurrentRoutineIdx on a completed cycle!" }
    currentRoutineIdx = cycle.cycleList
        .firstOrNull { !it.isComplete }
        ?.run { cycle.cycleList.indexOf(this) } ?: 0    // When cycle is done but not yet marked as complete - go to first item
  }

  private fun startPerforming() {
    performManager.startPerforming(getCurrentRoutine().durationTimeSec,
        CountDownNotification.InitData(R.string.cycle_routine_ongoing_notification_title, SerieActivity::class.java.name, DUMMY_REQUESTCODE, CountDownNotification.PERFORMING_NOTIFICATION_ID))
    cycleStateEventsProcessor.onNext(CycleState.PERFORMING)
  }
}