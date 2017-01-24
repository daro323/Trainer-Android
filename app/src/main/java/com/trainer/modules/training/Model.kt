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
  fun status() = when {
    series.all { it.status() == NEW } -> NEW
    series.all { it.status() == COMPLETE } -> COMPLETE
    series.any { it.status() == STARTED || it.status() == COMPLETE } -> STARTED
    else -> throw IllegalStateException("Can't determine status of Workout= $this")
  }
}

data class Exercise(val name: String,
                    val comments: List<String> = emptyList(),
                    val imageInfo: ExerciseImageMap = ExerciseImageMap.DEFAULT_IMAGE,
                    val weightType: WeightType = KG) {

  fun imageResource() = imageInfo.resource
}

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
  fun status(): ProgressStatus
  fun skipRemaining()
  fun complete()
  fun abort()

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
                    lastProgress: List<Repetition> = (1..seriesCount).map { emptyRepetition(exercise) }.toList()) = Set((++instanceCounter).toString(), exercise, guidelines, seriesCount, restTimeSec, progress, lastProgress)

      private fun emptyRepetition(forExercise: Exercise) = Repetition(0f, 0, forExercise.weightType)
    }

    override fun id() = _id

    override fun status() = when {
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

    override fun abort() {
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

    override fun status() = when {
      setList.all { it.status() == NEW } -> NEW
      setList.all { it.status() == COMPLETE } -> COMPLETE
      setList.any { it.status() == STARTED || it.status() == COMPLETE } -> STARTED
      else -> throw IllegalStateException("Can't determine status of SuperSet= $this")
    }

    override fun skipRemaining() = setList.forEach(Series::skipRemaining)

    override fun complete() = setList.forEach(Series::complete)

    override fun abort() = setList.forEach(Series::abort)

    override fun equals(other: Any?) = other is SuperSet && other.id() == this.id()

    override fun hashCode() = setList.hashCode()
  }
}

enum class ExerciseImageMap(@DrawableRes val resource: Int) {
  // CHEST
  DEFAULT_IMAGE(R.mipmap.ic_exercise_default),
  BENCH_PRESS_IMAGE(R.drawable.ex_bench_press),
  PULL_UPS_IMAGE(R.drawable.ex_pullups),
  INCLINE_DUMBELL_PRESS_IMAGE(R.drawable.ex_incline_press),
  SINGLE_DUMBELL_ROW_IMAGE(R.drawable.ex_single_dumbell_row),
  SWEED_PUSHUP_IMAGE(R.drawable.ex_sweed_pushup),
  DUMBELL_PUSHUP_IMAGE(R.drawable.ex_dumbell_pushup),
  TRICEPS_EXTENSIONS_IMAGE(R.drawable.ex_triceps_extensions),
  TRICEPS_PULLDOWN_IMAGE(R.drawable.ex_triceps_pulldown),
  SUPINATED_BICEPS_CURL_IMAGE(R.drawable.ex_supinated_biceps_curl),

  // ARMS
  NARROW_GRIP_PULLUP_IMAGE(R.drawable.ex_narrow_grip_pullup),
  SEATED_BARBELL_SHOULDER_PRESS_IMAGE(R.drawable.ex_barbell_shoulder_press),
  BARBELL_ROW_IMAGE(R.drawable.ex_barbell_row),
  BARBELL_BICEPS_CURL_IMAGE(R.drawable.ex_barbell_biceps_curl),

  // BACK
  KNEELIN_BACK_ROTATIONS_IMAGE(R.drawable.ex_kneeling_back_rotations),
  DEADLIFT_IMAGE(R.drawable.ex_deadlift),
  DUMBELLS_ROW_IMAGE(R.drawable.ex_dumbells_row),
  PAUSED_PULL_UPS_IMAGE(R.drawable.ex_pullups),
  CABLE_ARM_RAISE_IMAGE(R.drawable.ex_cable_arm_raise),

  // LEGS
  FRONT_SQUAT_IMAGE(R.drawable.ex_front_squat),
  MACHINE_LEG_PRESS_IMAGE(R.drawable.ex_machine_leg_press),
  DUMBELL_WALKING_LUNGES_IMAGE(R.drawable.ex_dumbell_walking_lunges),
  LEG_CURLS_IMAGE(R.drawable.ex_leg_curls),
  CALF_RAISE_IMAGE(R.drawable.ex_calf_raise),

  // SHOULDERS
  SEATED_DUMBELL_SHOULDER_PRESS_IMAGE(R.drawable.ex_dumbell_shoulder_press),
  BARBELL_TO_CHEST_PULL_IMAGE(R.drawable.ex_barbell_to_chest_pull),
  DUMBELL_SHOULDER_RAISE_IMAGE(R.drawable.ex_dumbell_shoulder_raise),
  CABLE_TO_HEAD_PULL_IMAGE(R.drawable.ex_cable_to_head_pull),
  LYING_DUMBELL_ROTATIONS_IMAGE(R.drawable.ex_lying_dumbell_rotations);
}