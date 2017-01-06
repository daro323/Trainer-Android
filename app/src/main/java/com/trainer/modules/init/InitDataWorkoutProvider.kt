package com.trainer.modules.init

import android.util.Log
import com.trainer.modules.init.data.ChestInitData
import com.trainer.modules.training.TrainingCategory
import com.trainer.modules.training.TrainingCategory.CHEST
import com.trainer.modules.training.Workout
import com.trainer.modules.training.WorkoutProvider

/**
 * Created by dariusz on 05/01/17.
 */
class InitDataWorkoutProvider : WorkoutProvider {

  override fun provideFor(trainingCategory: TrainingCategory): Workout {
    return when(trainingCategory) {
      CHEST -> ChestInitData.CHEST_WORKOUT
      else -> {
        Log.w("INIT_DATA_PROVIDER", "No init data available for training= $trainingCategory - returning an empty workout...")
        Workout(emptyList())
      }
    }
  }
}