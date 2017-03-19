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

  override fun skipRemaining() { /* Do nothing and keep the current cyclesCount */
  }

  fun start() {
    isComplete = false
    cyclesCount = 0
  }

  override fun complete() {
    require(cyclesCount > 0) { "Attempt to mark Cycle as complete when there are not cycles complete!" }
    lastCyclesCount = cyclesCount
    cyclesCount = -1
    isComplete = true
  }

  override fun abort() {
    cyclesCount = -1
    isComplete = true
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
                                     var countDownTime: Int = durationTimeSec)