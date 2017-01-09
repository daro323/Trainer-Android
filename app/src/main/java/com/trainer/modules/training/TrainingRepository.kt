package com.trainer.modules.training

import android.content.SharedPreferences
import com.fasterxml.jackson.databind.ObjectMapper
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.saveString
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingRepository @Inject constructor(val sharedPrefs: SharedPreferences,
                                             val mapper: ObjectMapper) {

  fun getTrainingDay(forCategory: TrainingCategory): TrainingDay {
    val trainingDayJson = sharedPrefs.getString(forCategory.toString(), null)
    require(trainingDayJson != null) { "No Training day found for $forCategory" }

    return mapper.readValue(trainingDayJson, TrainingDay::class.java)
  }

  /**
   * Replaces currently stored Training day with the one from parameter.
   * It expects to have the current workout cleaned out (workout data moved to lastWorkout)
   */
  fun saveTrainingDay(trainingDay: TrainingDay) {
    require(trainingDay.workout.isStarted().not()) { "Attempt to save a TrainingDay which is not cleaned up!" }
    require(trainingDay.lastWorkout.isComplete()) { "Attempt to save a TrainingDay with last training not complete!" }
    sharedPrefs.saveString(trainingDay.category.toString(), mapper.writeValueAsString(trainingDay))
  }
}