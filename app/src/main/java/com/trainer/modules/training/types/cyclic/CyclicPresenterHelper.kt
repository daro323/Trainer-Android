package com.trainer.modules.training.types.cyclic

import com.trainer.core.training.business.WorkoutPresenterHelper
import com.trainer.core.training.model.ProgressStatus
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.WorkoutEvent
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CyclicPresenterHelper @Inject constructor() : WorkoutPresenterHelper {
  override fun initWith(serie: Serie, callback: WorkoutPresenterHelper.HelperCallback) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
}