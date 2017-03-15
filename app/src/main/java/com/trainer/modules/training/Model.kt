package com.trainer.modules.training

import android.support.annotation.DrawableRes
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.trainer.R
import com.trainer.extensions.daysSince
import com.trainer.modules.training.ProgressStatus.*
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.WeightType.KG
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

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
  DO_NEXT_SERIE,
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
                       private var totalDone: Int = 0,
                       private var lastTrainedDate: String? = null) {

  companion object {
    private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
  }

  fun updateAsDone() {
    totalDone++
    lastTrainedDate = LocalDate.now().format(DATE_FORMATTER)
  }

  fun getTotalDone() = totalDone

  fun trainedDaysAgo() = LocalDate.now().daysSince(lastTrainedDate?.run { LocalDate.parse(this, DATE_FORMATTER) } ?: LocalDate.now()).toInt()
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

  companion object {
    var instanceCounter: Int = 0
    /* Automatically adds IDs as instance count */
    fun createSet(exercise: Exercise,
                  guidelines: List<String>,
                  seriesCount: Int,
                  restTimeSec: Int,
                  progress: MutableList<Repetition> = mutableListOf(),
                  lastProgress: List<Repetition> = (1..seriesCount).map { emptyRepetition(exercise) }.toList()) = Set((++instanceCounter).toString(), exercise, guidelines, seriesCount, restTimeSec, progress, lastProgress)

    /* Automatically adds IDs as instance count */
    fun createCycle(cycleList: List<CyclicRoutine>,
                    totalCycles: Int,
                    restTime: Int,
                    cyclesCount: Int = -1,
                    lastCyclesCount: Int = 0) = Cycle((++instanceCounter).toString(), cycleList, totalCycles, cyclesCount, lastCyclesCount, restTime)

    private fun emptyRepetition(forExercise: Exercise) = Repetition(0f, 0, forExercise.weightType)
  }

  data class Set constructor(val _id: String,
                             val exercise: Exercise,
                             val guidelines: List<String>,
                             val seriesCount: Int,
                             val restTimeSec: Int,
                             var progress: MutableList<Repetition>,
                             var lastProgress: List<Repetition>) : Series {

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

  abstract class CompositeSeries<out T : Series> constructor(val seriesList: List<T>) : Series {
    override fun id() = seriesList
        .map(Series::id)
        .reduce { acc, item -> "$acc$item" }

    override fun status() = when {
      seriesList.all { it.status() == NEW } -> NEW
      seriesList.all { it.status() == COMPLETE } -> COMPLETE
      seriesList.any { it.status() == STARTED || it.status() == COMPLETE } -> STARTED
      else -> throw IllegalStateException("Can't determine status of SuperSet= $this")
    }

    override fun skipRemaining() = seriesList.forEach(Series::skipRemaining)

    override fun complete() = seriesList.forEach(Series::complete)

    override fun abort() = seriesList.forEach(Series::abort)

    override fun equals(other: Any?) = other is SuperSet && other.id() == this.id()

    override fun hashCode() = seriesList.hashCode()
  }

  class SuperSet(val setList: List<Set>) : CompositeSeries<Set>(setList)

  class Cycle(val _id: String,
              val cycleList: List<CyclicRoutine>,
              val totalCycles: Int,
              var cyclesCount: Int,
              val lastCyclesCount: Int,
              val restTime: Int) : Series {

    override fun id() = _id

    override fun status() = when {
      cyclesCount == -1 -> NEW
      cyclesCount < totalCycles -> STARTED
      cyclesCount == totalCycles -> COMPLETE
      else -> throw IllegalStateException("Invalid cycles count= $cyclesCount (totalCycles set to $totalCycles)")
    }

    override fun skipRemaining() { /* Do nothing and keep the current cyclesCount */
    }

    override fun complete() {
      require(cyclesCount == totalCycles) { "Attempt to mark Cycle as complete when there is still some missing progress!" }
    }

    override fun abort() {
      cyclesCount = -1
    }

    override fun equals(other: Any?) = other is Cycle && other.id() == this.id()
    override fun hashCode() = _id.hashCode().run {
      var result = this + cycleList.hashCode()
      result = 31 * result + totalCycles
      result = 31 * result + cyclesCount
      result = 31 * result + lastCyclesCount
      result = 31 * result + restTime
      result
    }
  }
}

data class StretchExercise private constructor(val id: String,
                                               val name: String,
                                               val guidelines: List<String>,
                                               val comments: List<String>,
                                               @DrawableRes val imageRes: Int) {
  companion object {
    var instanceCounter: Int = 0
    /* Automatically adds IDs as instance count */
    fun createStretch(@DrawableRes imageRes: Int = R.mipmap.ic_exercise_default,
                      guidelines: List<String> = arrayListOf("Roźciągaj 30 sekund."),
                      name: String = "Ćwiczenie",
                      comments: List<String> = emptyList()) = StretchExercise((++instanceCounter).toString(), name, guidelines, comments, imageRes)
  }
}

data class StretchRoutine(val category: TrainingCategory,
                          val stretchExercises: List<StretchExercise>)

data class StretchPlan(val stretchRoutines: List<StretchRoutine>) {

  fun getStretchRoutine(forCategory: TrainingCategory) = stretchRoutines.find { it.category == forCategory }
}

data class CyclicRoutine constructor(val exercise: Exercise,
                                     val restTimeSec: Int,
                                     val durationTimeSec: Int,
                                     var countDownTime: Int = durationTimeSec)

enum class ExerciseImageMap(@DrawableRes val resource: Int) {
  // CHEST
  DEFAULT_IMAGE(R.mipmap.ic_exercise_default),
  BENCH_PRESS_IMAGE(R.drawable.ex_bench_press),
  PULL_UPS_IMAGE(R.drawable.ex_pullups),
  INCLINE_DUMBELL_PRESS_IMAGE(R.drawable.ex_incline_press),
  SINGLE_DUMBELL_ROW_IMAGE(R.drawable.ex_single_dumbell_row),
  CHEST_DIPS_IMAGE(R.drawable.ex_chest_dips),
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
  DEADLIFT_IMAGE(R.drawable.ex_deadlift),
  DUMBELLS_ROW_IMAGE(R.drawable.ex_dumbells_row),
  PAUSED_PULL_UPS_IMAGE(R.drawable.ex_pullups),
  CABLE_ARM_RAISE_IMAGE(R.drawable.ex_cable_arm_raise),

  // LEGS
  FRONT_SQUAT_IMAGE(R.drawable.ex_front_squat),
  MACHINE_LEG_PRESS_IMAGE(R.drawable.ex_machine_leg_press),
  BULGARIAN_SPLIT_SQUAT_IMAGE(R.drawable.ex_bulgarian_split_squat),
  LEG_CURLS_IMAGE(R.drawable.ex_leg_curls),
  CALF_RAISE_IMAGE(R.drawable.ex_calf_raise),

  // SHOULDERS
  SEATED_DUMBELL_SHOULDER_PRESS_IMAGE(R.drawable.ex_dumbell_shoulder_press),
  DUMBELL_SHOULDER_SIDE_RAISE_IMAGE(R.drawable.ex_dumbell_shoulder_side_raise),
  DUMBELL_SHOULDER_RAISE_IMAGE(R.drawable.ex_dumbell_shoulder_raise),
  CABLE_TO_HEAD_PULL_IMAGE(R.drawable.ex_cable_to_head_pull),
  LYING_DUMBELL_ROTATIONS_IMAGE(R.drawable.ex_lying_dumbell_rotations);
}