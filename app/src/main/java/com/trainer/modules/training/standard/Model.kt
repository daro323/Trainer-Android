package com.trainer.modules.training.standard

import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import com.trainer.R
import com.trainer.modules.training.coredata.*
import com.trainer.modules.training.coredata.ProgressStatus.*

/**
 * Created by dariusz on 15/03/17.
 */
@Keep
data class Set constructor(val _id: String,
                           val exercise: Exercise,
                           val guidelines: List<String>,
                           val seriesCount: Int,
                           val restTimeSec: Int,
                           var progress: MutableList<Repetition>,
                           var lastProgress: List<Repetition>) : Serie {

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

@Keep
class SuperSet(val setList: List<Set>) : CompositeSerie<Set>(setList)

@Keep
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

@Keep
data class StretchRoutine(val category: TrainingCategory,
                          val stretchExercises: List<StretchExercise>)

@Keep
data class StretchPlan(val stretchRoutines: List<StretchRoutine>) {

  fun getStretchRoutine(forCategory: TrainingCategory) = stretchRoutines.find { it.category == forCategory }
}

@Keep
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