package com.trainer.modules.init.buffplan

import android.support.annotation.Keep
import com.trainer.modules.training.workout.WorkoutManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dariusz on 05/01/17.
 */
@Singleton
class CardioPlanInitializer @Inject constructor(val workoutManager: WorkoutManager) {
  companion object {
    const private val INIT_WORKOUT_PLAN_NAME = "Cardio Plan"
  }

  private fun isPlanInitialized(initCheckAction: () -> Unit): Boolean {
    try {
      initCheckAction()
      return true
    } catch (e: IllegalArgumentException) {
      return false
    }
  }
}

@Keep
enum class CardioCategories {
  KETTLEBELL,
  BOXING,
  LANDMINE
}