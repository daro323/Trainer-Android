package com.trainer.modules.training

import android.support.annotation.DrawableRes
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Created by dariusz on 05/01/17.
 */

enum class Training {
  CHEST,
  BACK,
  SHOULDERS,
  ARMS,
  LEGS
}

data class TrainingDay(val name: Training,
                       val workout: Workout,
                       val lastWorkout: Workout = Workout(emptyList()),
                       val totalDone: Int = 0)

data class Workout(val series: List<Series>)

data class Exercise(val name: String,
                    val comments: List<String>,
                    @DrawableRes val imageRes: Int)

data class Repetition(val repCount: Int,
                      val weight: String)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Series.Set::class, name = "Set"),
    JsonSubTypes.Type(value = Series.SuperSet::class, name = "SuperSet"))
interface Series {
  data class Set(val exercise: Exercise,
                 val guidelines: String,
                 val seriesCount: Int,
                 val restTimeSec: Int,
                 val progress: List<Repetition> = emptyList(),
                 val comments: List<String> = emptyList()) : Series

  data class SuperSet(val seriesList: List<Series>) : Series
}

interface WorkoutProvider {
  fun provideFor(training: Training): Workout
}