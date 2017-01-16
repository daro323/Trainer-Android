package com.trainer.modules.training

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.ProgressStatus.NEW
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by dariusz on 06/01/17.
 */
@ApplicationScope
class TrainingManager @Inject constructor(val repo: TrainingRepository,
                                          val workoutPresenterProvider: Provider<WorkoutPresenter>) {
  var workoutPresenter: WorkoutPresenter? = null
    private set

  fun startWorkout(forCategory: TrainingCategory) {
    require(isInitialized().not()) { "Can't start new workout - there is one already started!" }
    workoutPresenter = workoutPresenterProvider
        .get()
        .apply { trainingDay = repo.getTrainingPlan().getTrainingDay(forCategory) ?: throw IllegalStateException("trainingPlan not initialized!") }
  }

  /**
   * Automatically starts training for already started workout.
   */
  fun continueTraining(): Boolean {
    return getTrainingPlan().run {
      val alreadyStartedDay = trainingDays.find { it.workout.getStatus() != NEW }
      if (alreadyStartedDay != null) {
        startWorkout(alreadyStartedDay.category)
        return true
      } else {
        return false
      }
    }
  }

  fun abortWorkout() {
    workoutPresenter?.apply {
      trainingDay.workout.series
          .filter { it.getStatus() != NEW }
          .forEach(Series::abort)

      repo.saveTrainingPlan()
    }
    workoutPresenter = null
  }

  fun completeWorkout() {
    require(isInitialized()) { "Can't complete workout - there is no workout started!" }

    workoutPresenter?.apply {
      // Clean up the TrainingDay
      getWorkoutList().forEach(Series::complete)

      // Increase totalDone count
      trainingDay.increaseDoneCount()

      repo.saveTrainingPlan()
      reset()
    }
  }

  fun getTrainingPlan() = repo.getTrainingPlan()

  fun setTrainingPlan(trainingPlan: TrainingPlan) {
    reset()
    repo.setNewTrainingPlan(trainingPlan)
  }

  private fun isInitialized() = workoutPresenter != null

  private fun reset() {
    workoutPresenter = null
  }
}