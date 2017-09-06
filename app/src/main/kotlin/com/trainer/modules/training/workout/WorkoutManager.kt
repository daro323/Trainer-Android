package com.trainer.modules.training.workout

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.plan.TrainingRepository
import com.trainer.modules.training.workout.ProgressStatus.NEW
import com.trainer.modules.training.workout.types.standard.StretchPlan
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by dariusz on 06/01/17.
 */
@ApplicationScope
class WorkoutManager @Inject constructor(val repo: TrainingRepository,
                                         val workoutPresenterProvider: Provider<WorkoutPresenter>) {
  var workoutPresenter: WorkoutPresenter? = null
    private set

  fun startWorkout(forCategory: String) {
    // TODO Refactor
    /*require(isInitialized().not()) { "Can't start new workout - there is one already started!" }
    workoutPresenter = workoutPresenterProvider
        .get()
        .apply { trainingDay = repo.getTrainingPlan().getTrainingDay(forCategory) ?: throw IllegalStateException("trainingPlan not initialized!") }*/
  }

  /**
   * Automatically starts training for already started workout.
   */
  fun continueWorkout(): Boolean {
    // TODO Refactor
    return false
    /*return getTrainingPlan().run {
      val alreadyStartedDay = trainingDays.find { it.workout.status() != NEW }
      if (alreadyStartedDay != null) {
        startWorkout(alreadyStartedDay.category)
        return true
      } else {
        return false
      }
    }*/
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
      getWorkoutList().forEach(Serie::completeAndReset)

      // Increase totalDone count
      trainingDay.updateAsDone()

      repo.saveTrainingPlan()
      reset()
    }
    workoutPresenter = null
  }

  private fun reset() {
    workoutPresenter = null
  }

  private fun isInitialized() = workoutPresenter != null

  // Move this to the TrainingPlanManager ---------------------

  fun getTrainingPlan() = repo.getTrainingPlan()

  fun setTrainingPlan(trainingPlan: TrainingPlan) {
    reset()
    repo.setNewTrainingPlan(trainingPlan)
  }

  fun getStretchPlan() = repo.getStretchPlan()

  fun hasStretchPlan() = repo.hasStretchPlan()

  fun setStretchPlan(stretchPlan: StretchPlan) {
    repo.saveStretchPlan(stretchPlan)
  }
}