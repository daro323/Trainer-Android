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
  }

  fun getTrainingPlan(): TrainingPlan {
    val trainingPlanJson = sharedPrefs.getString(PLAN_NAME_KEY, null)
    require(trainingPlanJson != null) { "No Training Plan found" }

    return mapper.readValue(trainingPlanJson, TrainingPlan::class.java)
  }

  /**
   * Replaces currently stored Training Plan with the one from parameter.
   */
  fun saveTrainingPlan(trainingPlan: TrainingPlan) {
    sharedPrefs.saveString(PLAN_NAME_KEY, mapper.writeValueAsString(trainingPlan))
  }
}