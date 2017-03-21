package com.trainer.modules.training.types.cyclic

import android.support.annotation.Nullable
import com.trainer.core.training.business.WorkoutPresenterHelper
import com.trainer.core.training.model.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.core.training.model.ProgressStatus.COMPLETE
import com.trainer.core.training.model.Serie
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CyclicPresenterHelper @Inject constructor(val performManager: PerformManager) : WorkoutPresenterHelper {
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
    if (cycle.status() != COMPLETE) refreshCurrentRoutineIdx() else currentRoutineIdx = DEFAULT_ROUTINE_INDEX
  }

  override fun getRestTime() = cycle.restTimeSec

  override fun getSerie() = cycle

  override fun determineNextStep() {
    cycleStateEventsProcessor.onNext(if (isCurrentRoutineTheLast()) CycleState.DONE else CycleState.RESTING)
  }

  fun getCurrentRoutine() = cycle.cycleList[currentRoutineIdx]

  @Nullable
  fun getNextRoutine() = if (isCurrentRoutineTheLast()) null else cycle.cycleList[currentRoutineIdx + 1]

  fun getCycleRoutinesCount() = cycle.cycleList.size

  fun getCurrentCycleNumber() = cycle.cycleList.indexOf(getCurrentRoutine()) + 1

  fun onRoutineComplete() {
    getCurrentRoutine().isComplete = true
    callback.onSaveSerie(cycle)
  }

  fun onStartCycle() {
    require(currentRoutineIdx != VALUE_NOT_SET) { "Attempt to start cycle when current cycle routine is not set!" }
    cycleStateEventsProcessor.onNext(CycleState.GET_READY)
  }

  fun onCompleteCycle() {
    cycle.complete()
    callback.onSaveSerie(cycle)
  }

  fun onPrepared() {
    require(currentRoutineIdx != VALUE_NOT_SET) { "Attempt to start cycle when current cycle routine is not set!" }
    performManager.startPerforming(getCurrentRoutine().durationTimeSec)
  }

  fun onRested() {
    refreshCurrentRoutineIdx()
    cycleStateEventsProcessor.onNext(CycleState.PERFORMING)
  }

  fun getPerformEvents() = performManager.getPerformEvents()

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