package com.trainer.modules.training.types.cyclic

import android.support.annotation.Keep
import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.ProgressStatus.*
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.SerieType
import com.trainer.core.training.model.SerieType.CYCLE

/**
 * Created by dariusz on 15/03/17.
 */
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

  override fun complete() {
    lastCyclesCount = cyclesCount
    cyclesCount = -1
    cycleList.forEach { it.resetComplete() }
    isComplete = true
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
                                     val restTimeSec: Int,
                                     var isComplete: Boolean = false,
                                     var countDownTime: Int = durationTimeSec) {
  fun complete() {
    isComplete = true
  }

  fun resetComplete() {
    isComplete = false
  }
}