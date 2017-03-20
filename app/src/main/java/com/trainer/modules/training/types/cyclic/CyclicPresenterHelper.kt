package com.trainer.modules.training.types.cyclic

import com.trainer.core.training.business.WorkoutPresenterHelper
import com.trainer.core.training.model.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.core.training.model.ProgressStatus
import com.trainer.core.training.model.ProgressStatus.COMPLETE
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.WorkoutEvent.*
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CyclicPresenterHelper @Inject constructor() : WorkoutPresenterHelper {
  private lateinit var cycle: Cycle
  private lateinit var callback: WorkoutPresenterHelper.HelperCallback
  private var currentRoutineIdx = VALUE_NOT_SET

  companion object {
    const val DEFAULT_ROUTINE_INDEX = 0
  }

  override fun initWith(serie: Serie, callback: WorkoutPresenterHelper.HelperCallback) {
    require(serie is Cycle) { "CyclicPresenterHelper expects to be initialized with Cycle Serie type! (instead it was ${serie.javaClass.name})" }
    cycle = serie as Cycle
    this.callback = callback
    if (cycle.status() != COMPLETE) refreshCurrentRoutineIdx() else currentRoutineIdx = DEFAULT_ROUTINE_INDEX
  }

  override fun determineNextStep(workoutStatus: ProgressStatus) =
      if (workoutStatus == COMPLETE) {
        WORKOUT_COMPLETED
      } else if (cycle.status() == COMPLETE) {
        currentRoutineIdx = DEFAULT_ROUTINE_INDEX
        SERIE_COMPLETED
      } else {
        refreshCurrentRoutineIdx()
        DO_NEXT
      }

  override fun getRestTime() = cycle.restTimeSec

  override fun getSerie() = cycle

  fun getCurrentRoutine() = cycle.cycleList[currentRoutineIdx]

  fun onRoutineComplete() {
    getCurrentRoutine().isComplete = true
    callback.onSaveSerie(cycle)
  }

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