package com.trainer.persistence

import android.arch.persistence.room.TypeConverter
import com.trainer.core.training.model.Workout
import com.trainer.d2.common.AppModule

/**
 * Created by dariusz on 29.08.17.
 */
class Converters {

  companion object {

    val mapper = AppModule.createJackson()

    @TypeConverter
    fun workoutFromJson(value: String) = mapper.readValue(value, Workout::class.java)

    @TypeConverter
    fun workoutToJson(workout: Workout) = mapper.writeValueAsString(workout)
  }
}