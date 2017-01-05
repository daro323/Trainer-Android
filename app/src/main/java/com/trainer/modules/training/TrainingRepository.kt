package com.trainer.modules.training

import android.content.SharedPreferences
import com.google.gson.Gson
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.saveString
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingRepository @Inject constructor(val sharedPrefs: SharedPreferences,
                                             val gson: Gson) {

  fun getTrainingDay(name: String): TrainingDay {
    val trainingDay = sharedPrefs.getString(name, null)
    require(trainingDay != null) { "No Training day found for name= $name" }

    return gson.fromJson(trainingDay, TrainingDay::class.java)
  }

  /**
   * Replaces currently stored Training day with the one from parameter.
   */
  fun saveTrainingDay(trainingDay: TrainingDay) {
    sharedPrefs.saveString(trainingDay.name, gson.toJson(trainingDay))
  }
}