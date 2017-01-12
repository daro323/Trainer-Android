package com.trainer.modules.init

import android.util.Log
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.TrainingRepository
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingDataInitializer @Inject constructor(val trainingRepo: TrainingRepository,
                                                  val initProvider: InitDataWorkoutProvider) {
  companion object {
    val TAG = "INIT"
  }

  init {
    if (isTrainingPlanInitialized().not()) {
      val trainingPlan = initProvider.provideTrainingPlan()
      Log.d(TAG, "initializing training plan= ${trainingPlan.name}")
      trainingRepo.saveTrainingPlan(trainingPlan)
    }
  }

  private fun isTrainingPlanInitialized(): Boolean {
    try {
      val trainingPlan = trainingRepo.getTrainingPlan()
      Log.d(TAG, "Training plan already initialized to= ${trainingPlan.name}")
      return true
    } catch (e: IllegalArgumentException) {
      Log.d(TAG, "Training plan not yet initialized.")
      return false
    }
  }
}