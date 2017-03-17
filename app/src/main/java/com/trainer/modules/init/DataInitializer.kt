package com.trainer.modules.init

import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import android.util.Log
import com.trainer.R
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.model.TrainingCategory
import com.trainer.core.training.model.TrainingCategory.*
import com.trainer.core.training.model.TrainingDay
import com.trainer.core.training.model.TrainingPlan
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.init.data.exercise.ArmsExerciseInitData.Companion.ARMS_WORKOUT
import com.trainer.modules.init.data.exercise.BackExerciseInitData.Companion.BACK_WORKOUT
import com.trainer.modules.init.data.exercise.ChestExerciseInitData.Companion.CHEST_WORKOUT
import com.trainer.modules.init.data.exercise.LegsExerciseInitData.Companion.LEGS_WORKOUT
import com.trainer.modules.init.data.exercise.ShouldersExerciseInitData.Companion.SHOULDERS_WORKOUT
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.ARMS_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.BACK_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.CHEST_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.LEGS_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.SHOULDERS_DAY_STRETCH_ROUTINE
import com.trainer.modules.training.standard.StretchPlan
import com.trainer.modules.training.standard.StretchRoutine
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class DataInitializer @Inject constructor(val trainingManager: TrainingManager) {
  companion object {
    const val TAG = "INIT"
    const private val INIT_WORKOUT_PLAN_NAME = "Menshilf Plan"
  }

  init {
    initializeTrainingPlan()
    initializeStretchPlan()
  }

  private fun isPlanInitialized(initCheckAction: () -> Unit): Boolean {
    try {
      initCheckAction()
      return true
    } catch (e: IllegalArgumentException) {
      return false
    }
  }

  private fun initializeTrainingPlan() {
    if (isPlanInitialized{ trainingManager.getTrainingPlan() }.not()) {
      val trainingPlan = provideTrainingPlan()
      Log.d(TAG, "initializing training plan= ${trainingPlan.name}")
      trainingManager.setTrainingPlan(trainingPlan)
    }
  }

  private fun provideTrainingPlan(): TrainingPlan {
    val initTrainingDays = TrainingCategory.values().flatMap { category -> listOf(provideTrainingDay(category)) }
    return TrainingPlan(INIT_WORKOUT_PLAN_NAME, initTrainingDays.toMutableList())
  }

  private fun provideTrainingDay(category: TrainingCategory): TrainingDay {
    return when(category) {
      CHEST -> TrainingDay(category, CHEST_WORKOUT)
      LEGS -> TrainingDay(category, LEGS_WORKOUT)
      BACK -> TrainingDay(category, BACK_WORKOUT)
      SHOULDERS -> TrainingDay(category, SHOULDERS_WORKOUT)
      ARMS -> TrainingDay(category, ARMS_WORKOUT)
    }
  }

  private fun initializeStretchPlan() {
    if (isPlanInitialized{ trainingManager.getStretchPlan() }.not()) {
      val stretchPlan = provideStretchPlan()
      Log.d(TAG, "initializing stretch plan")
      trainingManager.setStretchPlan(stretchPlan)
    }
  }

  private fun provideStretchPlan(): StretchPlan {
    val initStretchRoutines = TrainingCategory.values().flatMap { category -> listOf(provideStretchRoutine(category)) }
    return StretchPlan(initStretchRoutines)
  }

  private fun provideStretchRoutine(category: TrainingCategory): StretchRoutine {
    return when(category) {
      CHEST -> CHEST_DAY_STRETCH_ROUTINE
      LEGS -> LEGS_DAY_STRETCH_ROUTINE
      BACK -> BACK_DAY_STRETCH_ROUTINE
      SHOULDERS -> SHOULDERS_DAY_STRETCH_ROUTINE
      ARMS -> ARMS_DAY_STRETCH_ROUTINE
    }
  }
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