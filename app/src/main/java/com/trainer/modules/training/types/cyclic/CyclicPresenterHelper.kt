package com.trainer.modules.training.types.cyclic

import com.trainer.core.training.business.WorkoutPresenterHelper
import com.trainer.core.training.model.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.core.training.model.ProgressStatus
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.WorkoutEvent
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CyclicPresenterHelper @Inject constructor() : WorkoutPresenterHelper {
  private lateinit var cycle: Cycle
  private lateinit var callback: WorkoutPresenterHelper.HelperCallback
  private var currentRoutineIdx = VALUE_NOT_SET

  override fun initWith(serie: Serie, callback: WorkoutPresenterHelper.HelperCallback) {
    require(serie is Cycle) { "CyclicPresenterHelper expects to be initialized with Cycle Serie type! (instead it was ${serie.javaClass.name})" }
    cycle = serie as Cycle
    this.callback = callback
    if (cycle.status() != ProgressStatus.COMPLETE) refreshCurrentRoutineIdx() else currentRoutineIdx = 0
  }

  override fun determineNextStep(workoutStatus: ProgressStatus): WorkoutEvent {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getRestTime(): Int {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getSerie(): Serie {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun refreshCurrentRoutineIdx() {
  }
}