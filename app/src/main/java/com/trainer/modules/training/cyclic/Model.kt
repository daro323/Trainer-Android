package com.trainer.modules.training.cyclic

import android.support.annotation.Keep
import com.trainer.modules.training.coredata.Exercise
import com.trainer.modules.training.coredata.ProgressStatus.*
import com.trainer.modules.training.coredata.Serie

/**
 * Created by dariusz on 15/03/17.
 */
@Keep
data class Cycle(val _id: String,
            val cycleList: List<CyclicRoutine>,
            val totalCycles: Int,
            var cyclesCount: Int,
            val lastCyclesCount: Int,
            val restTime: Int) : Serie {

  override fun id() = _id

  override fun status() = when {
    cyclesCount == -1 -> NEW
    cyclesCount < totalCycles -> STARTED
    cyclesCount == totalCycles -> COMPLETE
    else -> throw IllegalStateException("Invalid cycles count= $cyclesCount (totalCycles set to $totalCycles)")
  }

  override fun skipRemaining() { /* Do nothing and keep the current cyclesCount */
  }

  override fun complete() {
    require(cyclesCount == totalCycles) { "Attempt to mark Cycle as complete when there is still some missing progress!" }
  }

  override fun abort() {
    cyclesCount = -1
  }

  override fun equals(other: Any?) = other is Cycle && other.id() == this.id()
  override fun hashCode() = _id.hashCode().run {
    var result = this + cycleList.hashCode()
    result = 31 * result + totalCycles
    result = 31 * result + cyclesCount
    result = 31 * result + lastCyclesCount
    result = 31 * result + restTime
    result
  }
}

@Keep
data class CyclicRoutine constructor(val exercise: Exercise,
                                     val restTimeSec: Int,
                                     val durationTimeSec: Int,
                                     var countDownTime: Int = durationTimeSec)