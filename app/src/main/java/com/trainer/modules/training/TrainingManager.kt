package com.trainer.modules.training

import com.trainer.d2.scope.ApplicationScope
import rx.Observable
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
    require(isWorkoutActive().not()) { "Can't start new workout - there is one already started!" }
    workoutPresenter = workoutPresenterProvider
        .get()
        .apply { trainingDay = repo.getTrainingDay(forCategory) }
  }

  fun isWorkoutActive() = workoutPresenter != null

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

      // Save training day
      repo.saveTrainingDay(this.trainingDay)
    }

    // Clear workout presenter
    workoutPresenter = null
  }

  fun getTrainingDays(): Observable<List<TrainingDay>> {
    return Observable.from(TrainingCategory.values())
        .map { repo.getTrainingDay(it) }
        .toList()
  }
}