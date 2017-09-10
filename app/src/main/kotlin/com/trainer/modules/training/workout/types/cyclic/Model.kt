package com.trainer.modules.training.workout.types.cyclic

import android.support.annotation.Keep
import com.trainer.modules.training.workout.ProgressStatus.*
import com.trainer.modules.training.workout.Serie
import com.trainer.modules.training.workout.SerieType
import com.trainer.modules.training.workout.SerieType.CYCLE
import com.trainer.persistence.training.TrainingPlanDao.Exercise

/**
 * Created by dariusz on 15/03/17.
 */
@Keep
enum class CycleState {
  NEW,        // User is presented starting screen
  GET_READY,  // Getting ready (3 to 0 count down is initiated)
  PERFORMING, // Performing a routine
  RESTING,    // Resting in between routines
  DONE,       // Cycle routine set was performed, at this point we can do another cycle
  COMPLETE    // Whole cycle is finished, at this point we can't perform another cycle
}

@Keep
data class Cycle(private val _id: String,
                 val name: String,
                 val cycleList: List<CyclicRoutine>,
                 val restTimeSec: Int,
                 var lastCyclesCount: Int,
                 var cyclesCount: Int = -1,
                 var isComplete: Boolean = false,
                 private val type: SerieType = CYCLE) : Serie {

  override fun id() = _id

  override fun name() = name

  override fun type() = type

  override fun status() = when {
    cyclesCount == -1 -> NEW
    cyclesCount >= 0 && isComplete.not() -> STARTED
    isComplete -> COMPLETE
    else -> throw IllegalStateException("Invalid cycles count= $cyclesCount")
  }

  override fun skipRemaining() {
    cycleList.forEach { it.resetComplete() }
    if (cyclesCount < 0) cyclesCount = 0    // Cycle wasn't even started
    isComplete = true
  }

  fun start() {
    cyclesCount = 0
    cycleList.forEach { it.resetComplete() }
    isComplete = false
  }

  fun startNext() {
    cycleList.forEach { it.resetComplete() }
  }

  fun done() {
    isComplete = true
  }

  override fun completeAndReset() {
    lastCyclesCount = cyclesCount
    cyclesCount = -1
    cycleList.forEach { it.resetComplete() }
    isComplete = false
  }

  override fun abort() {
    cyclesCount = -1
    cycleList.forEach { it.resetComplete() }
  }

  override fun equals(other: Any?) = other is Cycle && other.id() == this.id()
  override fun hashCode() = _id.hashCode().run {
    var result = this + cycleList.hashCode()
    result = 31 * result + cyclesCount
    result = 31 * result + lastCyclesCount
    result = 31 * result + restTimeSec
    result
  }
}

@Keep
data class CyclicRoutine constructor(val exercise: Exercise,
                                     val durationTimeSec: Int,
                                     val restTimeSec: Int = 0,
                                     var isComplete: Boolean = false) {
  fun complete() {
    isComplete = true
  }

  fun resetComplete() {
    isComplete = false
  }
}