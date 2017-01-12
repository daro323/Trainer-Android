package com.trainer.modules.training

import com.trainer.d2.scope.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by dariusz on 06/01/17.
 */
@ApplicationScope
class TrainingManager @Inject constructor(val repo: TrainingRepository,
                                          val workoutPresenterProvider: Provider<WorkoutPresenter>) {
  private var trainingPlan: TrainingPlan? = null
  var workoutPresenter: WorkoutPresenter? = null
    private set

  fun startWorkout(forCategory: TrainingCategory) {
    require(isWorkoutActive().not()) { "Can't start new workout - there is one already started!" }
    workoutPresenter = workoutPresenterProvider
        .get()
        .apply { trainingDay = getTrainingPlan()?.getTrainingDay(forCategory) ?: throw IllegalStateException("trainingPlan not loaded!") }
  }

  fun isWorkoutActive() = workoutPresenter != null && trainingPlan != null

  fun abortWorkout() {
    workoutPresenter = null
  }

  fun completeWorkout() {
    require(isWorkoutActive()) { "Can't complete workout - there is no workout started!" }

    workoutPresenter?.apply {
      // Clean up the TrainingDay
      this.getWorkoutList().forEach(Series::complete)

      // Increase totalDone count
      this.trainingDay.increaseDoneCount()
    }

    trainingPlan?.apply { repo.saveTrainingPlan(this) }
    reset()
  }

  fun getTrainingDays() = getTrainingPlan()?.trainingDays ?: throw IllegalStateException("trainingPlan not loaded!")

  fun getTrainingPlanName() = getTrainingPlan()?.name ?: throw IllegalStateException("trainingPlan not loaded!")

  private fun getTrainingPlan(): TrainingPlan? {
    if (trainingPlan == null) {
      trainingPlan = repo.getTrainingPlan()
    }
    return trainingPlan
  }

  private fun reset() {
    workoutPresenter = null
    trainingPlan = null
  }
}