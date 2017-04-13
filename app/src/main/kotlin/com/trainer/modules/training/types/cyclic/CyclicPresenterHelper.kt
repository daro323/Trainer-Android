package com.trainer.modules.training.types.cyclic

import android.support.annotation.Nullable
import com.trainer.core.training.business.WorkoutPresenterHelper
import com.trainer.core.training.model.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.core.training.model.ProgressStatus.*
import com.trainer.core.training.model.Serie
import com.trainer.modules.training.rest.RestManager
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
      performManager.startPerforming(getCurrentRoutine().durationTimeSec)
      cycleStateEventsProcessor.onNext(CycleState.PERFORMING)
    }
  }

  fun getCurrentRoutine() = cycle.cycleList[currentRoutineIdx]

  @Nullable
  fun getNextRoutine() = if (isCurrentRoutineTheLast()) null else cycle.cycleList[currentRoutineIdx + 1]

  fun getCycleRoutinesCount() = cycle.cycleList.size

  fun getCurrentCycleNumber() = cycle.cycleList.indexOf(getCurrentRoutine()) + 1

  fun onStartCycle() {
    require(currentRoutineIdx != VALUE_NOT_SET) { "Attempt to start cycle when current cycle routine is not set!" }
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

  fun onCompleteCycle() {
    cycle.complete()
    callback.onSaveSerie(cycle)
  }

  fun onPrepared() {
    require(currentRoutineIdx != VALUE_NOT_SET) { "Attempt to start cycle when current cycle routine is not set!" }
    performManager.startPerforming(getCurrentRoutine().durationTimeSec)
    cycle.start()
    cycleStateEventsProcessor.onNext(CycleState.PERFORMING)
  }

  fun getPerformEvents() = performManager.getPerformEvents()

  override fun getRestTime() = cycle.restTimeSec

  fun onStartRestBetweenRoutines() {
    restManager.startRest(getCurrentRoutine().restTimeSec, withVibration = false)
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
        .first { !it.isComplete }
        .run { cycle.cycleList.indexOf(this) }
  }
}