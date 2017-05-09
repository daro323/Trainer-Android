package com.trainer.modules.init.buffplan

import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import com.trainer.commons.Lg
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.model.TrainingDay
import com.trainer.core.training.model.TrainingPlan
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.init.cardioplan.data.BoxingExerciseInitData.Companion.BOXING_WORKOUT
import com.trainer.modules.init.cardioplan.data.KettlebellExerciseInitData.Companion.KETTLEBELL_WORKOUT
import com.trainer.modules.init.cardioplan.data.LandmineExerciseInitData.Companion.LANDMINE_WORKOUT
import com.trainer.modules.init.buffplan.CardioCategories.*
import java.util.*
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class CardioPlanInitializer @Inject constructor(val trainingManager: TrainingManager) {
  companion object {
    const private val INIT_WORKOUT_PLAN_NAME = "Cardio Plan"
  }

  init {
    // Initialization of pre-bundled plans
    initializeTrainingPlan()
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
    val initTrainingDays = CardioCategories.values().flatMap { category -> listOf(provideTrainingDay(category.toString())) }
    val set = HashSet<String>()
    (CardioCategories.values().flatMap { category -> listOf(category.toString()) })
        .run { forEach { set.add(it) } }

    return TrainingPlan(INIT_WORKOUT_PLAN_NAME, set, initTrainingDays.toMutableList())
  }

  private fun provideTrainingDay(category: String): TrainingDay {
    return when (CardioCategories.valueOf(category)) {
      KETTLEBELL -> TrainingDay(category, KETTLEBELL_WORKOUT)
      BOXING -> TrainingDay(category, BOXING_WORKOUT)
      LANDMINE -> TrainingDay(category, LANDMINE_WORKOUT)
    }
  }
}

@Keep
enum class CardioCategories {
  KETTLEBELL,
  BOXING,
  LANDMINE
}

@Keep
enum class CardioExerciseImageMap(@DrawableRes val resource: Int) {

}