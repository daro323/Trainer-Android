package com.trainer.modules.init

import android.util.Log
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.*
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingDataInitializer @Inject constructor(val trainingRepo: TrainingRepository,
                                                  val workoutProvider: WorkoutProvider) {
  companion object {
    val TAG = "INIT"
    private fun completeEmptyWorkout(workout: Workout) = workout.series.forEach(Series::skipRemaining)
  }

  init {
    TrainingCategory.values().forEach { training ->
      if (isTrainingInitialized(training).not()) {
        Log.d(TAG, "init $training")
        val emptyWorkout = workoutProvider.provide(training)
        val completedEmptyWorkout = workoutProvider.provide(training).apply { completeEmptyWorkout(this) }
        trainingRepo.saveTrainingDay(TrainingDay(training, emptyWorkout, completedEmptyWorkout))
      }
    }
  }

  private fun isTrainingInitialized(trainingCategory: TrainingCategory): Boolean {
    try {
      trainingRepo.getTrainingDay(trainingCategory)
      Log.d(TAG, "Training of type= $trainingCategory already initialized.")
      return true
    } catch (e: IllegalArgumentException) {
      Log.d(TAG, "Training of type= $trainingCategory in not yet initialized.")
      return false
    }
  }
}