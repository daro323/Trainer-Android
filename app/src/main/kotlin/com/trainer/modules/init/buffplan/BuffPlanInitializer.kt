package com.trainer.modules.init.buffplan

import android.support.annotation.Keep
import com.trainer.commons.Lg
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.model.TrainingDay
import com.trainer.core.training.model.TrainingPlan
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.init.buffplan.InitCategories.*
import com.trainer.modules.init.buffplan.data.exercise.ChestExerciseInitData.Companion.CHEST_WORKOUT
import com.trainer.modules.init.buffplan.data.exercise.LegsExerciseInitData.Companion.LEGS_WORKOUT
import com.trainer.modules.init.buffplan.data.exercise.ShouldersExerciseInitData.Companion.SHOULDERS_WORKOUT
import com.trainer.modules.init.buffplan.data.stretch.StretchInitData.Companion.ARMS_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.buffplan.data.stretch.StretchInitData.Companion.BACK_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.buffplan.data.stretch.StretchInitData.Companion.CHEST_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.buffplan.data.stretch.StretchInitData.Companion.LEGS_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.buffplan.data.stretch.StretchInitData.Companion.SHOULDERS_DAY_STRETCH_ROUTINE
import com.trainer.modules.init.data.exercise.ArmsExerciseInitData.Companion.ARMS_WORKOUT
import com.trainer.modules.init.data.exercise.BackExerciseInitData.Companion.BACK_WORKOUT
import com.trainer.modules.training.types.standard.StretchPlan
import com.trainer.modules.training.types.standard.StretchRoutine
import java.util.*
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class BuffPlanInitializer @Inject constructor(val trainingManager: TrainingManager) {
  companion object {
    const val TAG = "INIT"
    const private val INIT_WORKOUT_PLAN_NAME = "Buff Plan"
  }

  init {
    // Initialization of pre-bundled plans
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
    if (isPlanInitialized { trainingManager.getTrainingPlan() }.not()) {
      val trainingPlan = provideTrainingPlan()
      Lg.d("initializing training plan= ${trainingPlan.name}")
      trainingManager.setTrainingPlan(trainingPlan)
    }
  }

  private fun provideTrainingPlan(): TrainingPlan {
    val initTrainingDays = InitCategories.values().flatMap { category -> listOf(provideTrainingDay(category.toString())) }
    val set = HashSet<String>()
    (InitCategories.values().flatMap { category -> listOf(category.toString()) })
        .run { forEach { set.add(it) } }

    return TrainingPlan(INIT_WORKOUT_PLAN_NAME, set, initTrainingDays.toMutableList())
  }

  private fun provideTrainingDay(category: String): TrainingDay {
    return when (InitCategories.valueOf(category)) {
      CHEST -> TrainingDay(category, CHEST_WORKOUT)
      LEGS -> TrainingDay(category, LEGS_WORKOUT)
      BACK -> TrainingDay(category, BACK_WORKOUT)
      SHOULDERS -> TrainingDay(category, SHOULDERS_WORKOUT)
      ARMS -> TrainingDay(category, ARMS_WORKOUT)
    }
  }

  private fun initializeStretchPlan() {
    if (isPlanInitialized { trainingManager.getStretchPlan() }.not()) {
      val stretchPlan = provideStretchPlan()
      Lg.d("initializing menshilf stretch plan")
      trainingManager.setStretchPlan(stretchPlan)
    }
  }

  private fun provideStretchPlan(): StretchPlan {
    val initStretchRoutines = InitCategories.values().flatMap { category -> listOf(provideStretchRoutine(category.toString())) }
    return StretchPlan(initStretchRoutines)
  }

  private fun provideStretchRoutine(category: String): StretchRoutine {
    return when (valueOf(category)) {
      CHEST -> CHEST_DAY_STRETCH_ROUTINE
      LEGS -> LEGS_DAY_STRETCH_ROUTINE
      BACK -> BACK_DAY_STRETCH_ROUTINE
      SHOULDERS -> SHOULDERS_DAY_STRETCH_ROUTINE
      ARMS -> ARMS_DAY_STRETCH_ROUTINE
    }
  }
}

@Keep
enum class InitCategories {
  CHEST,
  BACK,
  SHOULDERS,
  ARMS,
  LEGS
}