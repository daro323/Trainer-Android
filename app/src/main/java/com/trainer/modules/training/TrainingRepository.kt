package com.trainer.modules.training

import android.content.SharedPreferences
import com.fasterxml.jackson.databind.ObjectMapper
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.saveString
import javax.inject.Inject

/**
 * Currently one plan at a time can be loaded into the app.
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingRepository @Inject constructor(val sharedPrefs: SharedPreferences,
                                             val mapper: ObjectMapper) {
  companion object {
    private val PLAN_NAME_KEY = "PLAN_NAME_KEY"
    private val PLAN_NOT_INITIALIZED = TrainingPlan("Not initialized", emptyList())
  }

  private var trainingPlan: TrainingPlan = PLAN_NOT_INITIALIZED

  fun getTrainingPlan(): TrainingPlan {
    if (trainingPlan == PLAN_NOT_INITIALIZED) {
      val trainingPlanJson = sharedPrefs.getString(PLAN_NAME_KEY, null)
      require(trainingPlanJson != null) { "No Training Plan found" }
      trainingPlan = mapper.readValue(trainingPlanJson, TrainingPlan::class.java)
    }
    return trainingPlan
  }

  /**
   * Replaces currently stored Training Plan with the one from parameter.
   * Requires input plan to have all the workouts for all training days finished.
   */
  fun setNewTrainingPlan(newPlan: TrainingPlan) {
    sharedPrefs.saveString(PLAN_NAME_KEY, mapper.writeValueAsString(newPlan))
    trainingPlan = newPlan
  }

  /**
   * Writes in memory Training Plan to persistence.
   */
  fun saveTrainingPlan() {
    require(trainingPlan != PLAN_NOT_INITIALIZED) { "Attempt to save uninitialized plan's progress!" }
    sharedPrefs.saveString(PLAN_NAME_KEY, mapper.writeValueAsString(trainingPlan))
  }
}