package com.trainer.persistence

import android.arch.persistence.room.TypeConverter
import com.fasterxml.jackson.core.type.TypeReference
import com.trainer.d2.common.AppModule
import com.trainer.modules.training.Workout


/**
 * Created by dariusz on 29.08.17.
 */
class Converters {

  companion object {
    val mapper = AppModule.createJackson()
  }

  @TypeConverter
  fun workoutFromJson(json: String): Workout = mapper.readValue(json, Workout::class.java)

  @TypeConverter
  fun workoutToJson(workout: Workout): String = mapper.writeValueAsString(workout)

  @TypeConverter
  fun stringSetToJson(stringSet: Set<String>): String = mapper.writeValueAsString(stringSet)

  @TypeConverter
  fun stringSetFromJson(json: String): Set<String> = mapper.readValue(json, object : TypeReference<Set<String>>() {})
}