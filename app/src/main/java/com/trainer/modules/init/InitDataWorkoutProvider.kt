package com.trainer.modules.init

import android.util.Log
import com.trainer.modules.init.data.ChestInitData
import com.trainer.modules.training.Training
import com.trainer.modules.training.Training.CHEST
import com.trainer.modules.training.Workout
import com.trainer.modules.training.WorkoutProvider

/**
 * Created by dariusz on 05/01/17.
 */
class InitDataWorkoutProvider : WorkoutProvider {

  override fun provideFor(training: Training): Workout {
    return when(training) {
      CHEST -> ChestInitData.CHEST_WORKOUT
      else -> {
        Log.w("INIT_DATA_PROVIDER", "No init data available for training= $training - returning an empty workout...")
        Workout(emptyList())
      }
    }
  }
}