package com.trainer.modules.init

import android.util.Log
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.init.data.exercise.ArmsExerciseInitData.Companion.ARMS_WORKOUT
import com.trainer.modules.init.data.exercise.BackExerciseInitData.Companion.BACK_WORKOUT
import com.trainer.modules.init.data.exercise.ChestExerciseInitData.Companion.CHEST_WORKOUT
import com.trainer.modules.init.data.exercise.LegsExerciseInitData.Companion.LEGS_WORKOUT
import com.trainer.modules.init.data.exercise.ShouldersExerciseInitData.Companion.SHOULDERS_WORKOUT
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.ARMS_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.BACK_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.CHEST_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.LEGS_STRETCH_ROUTINE
import com.trainer.modules.init.data.stretch.StretchInitData.Companion.SHOULDERS_STRETCH_ROUTINE
import com.trainer.modules.training.*
import com.trainer.modules.training.TrainingCategory.*
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
    return TrainingPlan(INIT_WORKOUT_PLAN_NAME, initTrainingDays)
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
      CHEST -> CHEST_STRETCH_ROUTINE
      LEGS -> LEGS_STRETCH_ROUTINE
      BACK -> BACK_STRETCH_ROUTINE
      SHOULDERS -> SHOULDERS_STRETCH_ROUTINE
      ARMS -> ARMS_STRETCH_ROUTINE
    }
  }
}