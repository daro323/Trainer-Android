package com.trainer.modules.training.plan

import android.content.SharedPreferences
import com.fasterxml.jackson.databind.ObjectMapper
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.saveString
import com.trainer.modules.training.workout.TrainingDay
import com.trainer.modules.training.workout.TrainingPlan
import com.trainer.modules.training.workout.types.standard.StretchPlan
import com.trainer.persistence.TrainingPlanDao
import javax.inject.Inject

/**
 * Currently one plan at a time can be loaded into the app.
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingRepository @Inject constructor(val trainingPlanDao: TrainingPlanDao,
                                             val sharedPrefs: SharedPreferences,
                                             val mapper: ObjectMapper) {

  // TODO: Remove deprecated stuff
  companion object {
    const private val TRAINING_PLAN_KEY = "TRAINING_PLAN_KEY"
    const private val STRETCH_PLAN_KEY = "STRETCH_PLAN_KEY"
    private val TRAINING_PLAN_NOT_INITIALIZED = TrainingPlan(1, "Not initialized", setOf())
    private val STRETCH_PLAN_NOT_INITIALIZED = StretchPlan(emptyList())
  }

  private var trainingPlan: TrainingPlan = TRAINING_PLAN_NOT_INITIALIZED
  private var stretchPlan: StretchPlan = STRETCH_PLAN_NOT_INITIALIZED

  fun getTrainingPlan(): TrainingPlan {
    if (trainingPlan == TRAINING_PLAN_NOT_INITIALIZED) {
      val trainingPlanJson = sharedPrefs.getString(TRAINING_PLAN_KEY, null)
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
    sharedPrefs.saveString(TRAINING_PLAN_KEY, mapper.writeValueAsString(newPlan))
    trainingPlan = newPlan
  }

  fun saveTrainingDay(day: TrainingDay) {
  }

  /**
   * Writes in memory Training Plan to persistence.
   */
  fun saveTrainingPlan() {
    require(trainingPlan != TRAINING_PLAN_NOT_INITIALIZED) { "Attempt to save uninitialized plan's progress!" }
    sharedPrefs.saveString(TRAINING_PLAN_KEY, mapper.writeValueAsString(trainingPlan))
  }

  fun saveStretchPlan(stretchPlan: StretchPlan) {
    sharedPrefs.saveString(STRETCH_PLAN_KEY, mapper.writeValueAsString(stretchPlan))
  }

  fun getStretchPlan(): StretchPlan {
    if (stretchPlan == STRETCH_PLAN_NOT_INITIALIZED) {
      val stretchPlanJson = sharedPrefs.getString(STRETCH_PLAN_KEY, null)
      require(stretchPlanJson != null) { "No Stretching Plan found" }
      stretchPlan = mapper.readValue(stretchPlanJson, StretchPlan::class.java)
    }
    return stretchPlan
  }

  fun hasStretchPlan() = sharedPrefs.getString(STRETCH_PLAN_KEY, null) != null
}