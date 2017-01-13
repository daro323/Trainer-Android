package com.trainer.modules.training

import android.support.annotation.DrawableRes
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.trainer.R
import com.trainer.modules.training.ProgressStatus.*
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.WeightType.KG

/**
 * Created by dariusz on 05/01/17.
 */

enum class WeightType {
  KG,
  BODY_WEIGHT   // For this the weight value is not applicable
}

enum class TrainingCategory {
  CHEST,
  BACK,
  SHOULDERS,
  ARMS,
  LEGS
}

enum class WorkoutEvent {
  REST,
  DO_NEXT_SET,
  SERIE_COMPLETED,
  WORKOUT_COMPLETED
}

enum class ProgressStatus {
  NEW,
  STARTED,
  COMPLETE
}

data class TrainingPlan(val name: String,
                        val trainingDays: List<TrainingDay>) {

  fun getTrainingDay(forCategory: TrainingCategory) = trainingDays.find { it.category == forCategory }
}

data class TrainingDay(val category: TrainingCategory,
                       val workout: Workout,
                       private var totalDone: Int = 0) {

  fun increaseDoneCount() { totalDone++ }

  fun getTotalDone() = totalDone
}

data class Workout(val series: List<Series>) {
  fun getStatus() = when {
    series.all { it.getStatus() == NEW } -> NEW
    series.all { it.getStatus() == COMPLETE } -> COMPLETE
    series.any { it.getStatus() == STARTED || it.getStatus() == COMPLETE } -> STARTED
    else -> throw IllegalStateException("Can't determine status of Workout= $this")
  }
}

data class Exercise(val name: String,
                    val comments: List<String>,
                    @DrawableRes val imageRes: Int = R.mipmap.ic_exercise_default,
                    val weightType: WeightType = KG)

data class Repetition(val weight: Float,
                      val repCount: Int,
                      val weightType: WeightType) {

  override fun toString() = when (weightType) {
    BODY_WEIGHT -> "$repCount reps"
    else -> "$weight $weightType  [x]  $repCount"
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Series.Set::class, name = "Set"),
    JsonSubTypes.Type(value = Series.SuperSet::class, name = "SuperSet"))
interface Series {
  fun id(): String
  fun getStatus(): ProgressStatus
  fun skipRemaining()
  fun complete()

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
                    lastProgress: List<Repetition> = (1..seriesCount).map { Repetition(0f, 0, exercise.weightType) }.toList()) = Set((++instanceCounter).toString(), exercise, guidelines, seriesCount, restTimeSec, progress, lastProgress)
    }

    override fun id() = _id

    override fun getStatus() = when {
      progress.isEmpty() -> NEW
      progress.size < seriesCount -> STARTED
      progress.size == seriesCount -> COMPLETE
      else -> throw IllegalStateException("Current progress= ${progress.size} exceeds total series count= $seriesCount")
    }

    override fun skipRemaining() {
      while (progress.size < seriesCount) progress.add(Repetition(0f, 0, exercise.weightType))
    }

    override fun complete() {
      require(progress.size == seriesCount) { "Attempt to mark set as complete when there is still some missing progress!" }
      lastProgress = progress
      progress = mutableListOf()
    }

    override fun equals(other: Any?) = other is Set && other.id() == this.id()

    override fun hashCode() = _id.hashCode().run {
      var result = 31 * this + exercise.hashCode()
      result = 31 * result + guidelines.hashCode()
      result = 31 * result + seriesCount
      result = 31 * result + restTimeSec
      result = 31 * result + progress.hashCode()
      result = 31 * result + lastProgress.hashCode()
      result
    }
  }

  data class SuperSet(val setList: List<Set>) : Series {
    override fun id() = setList
        .map(Series::id)
        .reduce { acc, item -> "$acc$item" }

    override fun getStatus() = when {
      setList.all { it.getStatus() == NEW } -> NEW
      setList.all { it.getStatus() == COMPLETE } -> COMPLETE
      setList.any { it.getStatus() == STARTED || it.getStatus() == COMPLETE } -> STARTED
      else -> throw IllegalStateException("Can't determine status of SuperSet= $this")
    }

    override fun skipRemaining() = setList.forEach(Series::skipRemaining)

    override fun complete() = setList.forEach(Series::complete)

    override fun equals(other: Any?) = other is SuperSet && other.id() == this.id()

    override fun hashCode() = setList.hashCode()
  }
}