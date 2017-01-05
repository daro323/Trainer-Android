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

  fun getTrainingDay(name: Training): TrainingDay {
    val trainingDayJson = sharedPrefs.getString(name.toString(), null)
    require(trainingDayJson != null) { "No Training day found for name= $name" }

    return mapper.readValue(trainingDayJson, TrainingDay::class.java)
  }

  /**
   * Replaces currently stored Training day with the one from parameter.
   */
  fun saveTrainingDay(trainingDay: TrainingDay) {
    sharedPrefs.saveString(trainingDay.name.toString(), mapper.writeValueAsString(trainingDay))
  }
}