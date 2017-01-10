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
                       val totalDone: Int = 0)

data class Workout(val series: List<Series>) {
  fun isStarted() = series.any(Series::isStarted)
  fun isComplete() = series.all(Series::isComplete)
}

data class Exercise(val name: String,
                    val comments: List<String>,
                    @DrawableRes val imageRes: Int)

data class Repetition(val repCount: Int,  // TODO: Add weight type info
                      val weight: Int) {

  override fun toString() = "$weight x $repCount"
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Series.Set::class, name = "Set"),
    JsonSubTypes.Type(value = Series.SuperSet::class, name = "SuperSet"))
interface Series {
  fun id(): String
  fun isStarted(): Boolean
  fun isComplete(): Boolean
  fun skipRemaining()

  data class Set private constructor(private val _id: String,
                                     val exercise: Exercise,
                                     val guidelines: List<String>,
                                     val seriesCount: Int,
                                     val restTimeSec: Int,
                                     var progress: MutableList<Repetition>,
                                     var lastProgress: List<Repetition>) : Series {
    companion object {
      var instanceCounter: Int = 0
      /* Automatically adds IDs as instance count */
      fun createSet(exercise: Exercise,
                    guidelines: List<String>,
                    seriesCount: Int,
                    restTimeSec: Int,
                    progress: MutableList<Repetition> = mutableListOf(),
                    lastProgress: List<Repetition> = (1..seriesCount).map { Repetition(0, 0) }.toList()) = Set((++instanceCounter).toString(), exercise, guidelines, seriesCount, restTimeSec, progress, lastProgress)
    }

    override fun id() = _id

    override fun isStarted() = progress.size in 1 until seriesCount

    override fun isComplete() = progress.size == seriesCount

    override fun skipRemaining() {
      while (progress.size < seriesCount) progress.add(Repetition(0, 0))
    }
  }

  data class SuperSet(val setList: List<Set>) : Series {
    override fun id() = setList
        .map(Series::id)
        .reduce { acc, item -> "$acc$item" }

    override fun isStarted() = setList.any(Series::isStarted)

    override fun isComplete() = setList.all(Series::isComplete)

    override fun skipRemaining() = setList.forEach(Series::skipRemaining)
  }
}

interface WorkoutProvider {
  fun provide(forTrainingCategory: TrainingCategory): Workout
}