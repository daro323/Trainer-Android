package com.trainer.core.training.business

import com.trainer.d2.scope.ApplicationScope
import com.trainer.core.training.model.ProgressStatus.NEW
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.TrainingPlan
import com.trainer.modules.training.types.standard.StretchPlan
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

  fun startWorkout(forCategory: String) {
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
      val alreadyStartedDay = trainingDays.find { it.workout.status() != NEW }
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
          .filter { it.status() != NEW }
          .forEach(Serie::abort)

      repo.saveTrainingPlan()
    }
    workoutPresenter = null
  }

  fun completeWorkout() {
    require(isInitialized()) { "Can't complete workout - there is no workout started!" }

    workoutPresenter?.apply {
      // Clean up the TrainingDay
      getWorkoutList().forEach(Serie::complete)

      // Increase totalDone count
      trainingDay.updateAsDone()

      repo.saveTrainingPlan()
      reset()
    }
    workoutPresenter = null
  }

  fun getTrainingPlan() = repo.getTrainingPlan()

  fun setTrainingPlan(trainingPlan: TrainingPlan) {
    reset()
    repo.setNewTrainingPlan(trainingPlan)
  }

  fun getStretchPlan() = repo.getStretchPlan()

  fun setStretchPlan(stretchPlan: StretchPlan) {
    repo.saveStretchPlan(stretchPlan)
  }

  private fun isInitialized() = workoutPresenter != null

  private fun reset() {
    workoutPresenter = null
  }
}