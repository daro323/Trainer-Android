package com.trainer.modules.init

import android.util.Log
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.Training
import com.trainer.modules.training.TrainingDay
import com.trainer.modules.training.TrainingRepository
import com.trainer.modules.training.WorkoutProvider
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingDataInitializer @Inject constructor(val trainingRepo: TrainingRepository,
                                                  val workoutProvider: WorkoutProvider) {

  init {
    Training.values().forEach { training ->
      if (isTrainingInitialized(training).not()) {
        Log.d(TAG, "init $training")
        trainingRepo.saveTrainingDay(TrainingDay(training, workoutProvider.provideFor(training)))
      }
    }
  }

  private fun isTrainingInitialized(training: Training): Boolean {
    try {
      trainingRepo.getTrainingDay(training)
      Log.d(TAG, "Training of type= $training already initialized.")
      return true
    } catch (e: IllegalArgumentException) {
      Log.d(TAG, "Training of type= $training in not yet initialized.")
      return false
    }
  }

  companion object {
    val TAG = "INIT"
  }
}