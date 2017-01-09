package com.trainer.modules.training

import android.support.annotation.DrawableRes
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Created by dariusz on 05/01/17.
 */

enum class TrainingCategory {
  CHEST,
  BACK,
  SHOULDERS,
  ARMS,
  LEGS
}

data class TrainingDay(val category: TrainingCategory,
                       val workout: Workout,
                       val lastWorkout: Workout = Workout(emptyList()),
                       val totalDone: Int = 0)

data class Workout(val series: List<Series>) {
  fun isStarted() = series.any(Series::isStarted)
  fun isComplete() = series.all(Series::isComplete)
}

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
  fun id(): String
  fun isStarted(): Boolean
  fun isComplete(): Boolean

  data class Set private constructor(val _id: String,
                                     val exercise: Exercise,
                                     val guidelines: String,
                                     val seriesCount: Int,
                                     val restTimeSec: Int,
                                     val progress: List<Repetition> = emptyList(),
                                     val comments: List<String> = emptyList()) : Series {
    companion object {
      var instanceCounter: Int = 0
      /* Automatically adds IDs as instance count */
      fun createSet(exercise: Exercise,
                    guidelines: String,
                    seriesCount: Int,
                    restTimeSec: Int,
                    progress: List<Repetition> = emptyList(),
                    comments: List<String> = emptyList()) = Set((++instanceCounter).toString(), exercise, guidelines, seriesCount, restTimeSec, progress, comments)
    }

    override fun id() = _id

    override fun isStarted() = progress.size in 1 until seriesCount

    override fun isComplete() = progress.size == seriesCount
  }

  data class SuperSet(val seriesList: List<Series>) : Series {
    override fun id() = seriesList
        .map(Series::id)
        .reduce { acc, item -> "$acc$item" }

    override fun isStarted() = seriesList.any(Series::isStarted)

    override fun isComplete() = seriesList.all(Series::isComplete)
  }
}

interface WorkoutProvider {
  fun provide(forTrainingCategory: TrainingCategory): Workout
}