package com.trainer.core.training.business

import com.trainer.core.training.model.ProgressStatus
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.WorkoutEvent

/**
 * Created by dariusz on 15/03/17.
 */
interface WorkoutPresenterHelper {
  fun initWith(serie: Serie, callback: HelperCallback)

  fun getRestTime(): Int

  fun determineNextStep(workoutStatus: ProgressStatus): WorkoutEvent

  interface HelperCallback {
    fun onSaveSerie(serie: Serie)
  }
}