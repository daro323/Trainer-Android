package com.trainer.modules.init

import android.util.Log
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.init.data.ChestInitData.Companion.CHEST_WORKOUT
import com.trainer.modules.init.data.LegsInitData.Companion.LEGS_WORKOUT
import com.trainer.modules.training.*
import com.trainer.modules.training.TrainingCategory.CHEST
import com.trainer.modules.training.TrainingCategory.LEGS
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingDataInitializer @Inject constructor(val trainingManager: TrainingManager) {
  companion object {
    val TAG = "INIT"
    private val INIT_WORKOUT_PLAN_NAME = "Menshilf Plan"
  }

  init {
    if (isTrainingPlanInitialized().not()) {
      val trainingPlan = provideTrainingPlan()
      Log.d(TAG, "initializing training plan= ${trainingPlan.name}")
      trainingManager.setTrainingPlan(trainingPlan)
    }
  }

  private fun isTrainingPlanInitialized(): Boolean {
    try {
      trainingManager.getTrainingPlan()?.apply { Log.d(TAG, "Training plan already initialized to= ${this.name}") }
      return true
    } catch (e: IllegalArgumentException) {
      Log.d(TAG, "Training plan not yet initialized.")
      return false
    }
  }

  private fun provideTrainingPlan(): TrainingPlan {
    val initTrainingDays = TrainingCategory.values().flatMap { category -> listOf(provideTrainingDay(category)) }
    return TrainingPlan(INIT_WORKOUT_PLAN_NAME, initTrainingDays)
  }

  private fun provideTrainingDay(forTrainingCategory: TrainingCategory): TrainingDay {
    return when(forTrainingCategory) {
      CHEST -> TrainingDay(forTrainingCategory, CHEST_WORKOUT)
      LEGS -> TrainingDay(forTrainingCategory, LEGS_WORKOUT)
      else -> {
        Log.w("INIT_DATA_PROVIDER", "No init data available for category= $forTrainingCategory - returning an empty workout...")
        TrainingDay(forTrainingCategory, Workout(emptyList()))
      }
    }
  }
}