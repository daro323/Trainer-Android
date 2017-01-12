package com.trainer.modules.init

import android.util.Log
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.init.data.ChestInitData
import com.trainer.modules.training.TrainingCategory
import com.trainer.modules.training.TrainingCategory.CHEST
import com.trainer.modules.training.TrainingDay
import com.trainer.modules.training.TrainingPlan
import com.trainer.modules.training.Workout
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class InitDataWorkoutProvider @Inject constructor() {

  companion object {
    private val INIT_WORKOUT_PLAN_NAME = "Menshilf Plan"
  }

  fun provideTrainingPlan(): TrainingPlan {
    val initTrainingDays = TrainingCategory.values().flatMap { category -> listOf(provideTrainingDay(category)) }
    return TrainingPlan(INIT_WORKOUT_PLAN_NAME, initTrainingDays)
  }

  private fun provideTrainingDay(forTrainingCategory: TrainingCategory): TrainingDay {
    return when(forTrainingCategory) {
      CHEST -> TrainingDay(forTrainingCategory, ChestInitData.CHEST_WORKOUT)
      else -> {
        Log.w("INIT_DATA_PROVIDER", "No init data available for category= $forTrainingCategory - returning an empty workout...")
        TrainingDay(forTrainingCategory, Workout(emptyList()))
      }
    }
  }
}