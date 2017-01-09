package com.trainer.modules.training

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.workout.WorkoutPresenter
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
    require(isWorkoutStarted().not()) { "Can't start new workout - there is one already started!" }
    workoutPresenter = workoutPresenterProvider
        .get()
        .apply { trainingDay = repo.getTrainingDay(forCategory) }
  }

  fun isWorkoutStarted() = workoutPresenter != null

  fun abortWorkout() {
    workoutPresenter = null
  }

  fun completeWorkout() { // TODO
    // Clean up the TrainingDay (move workout to last workout)

    // Increase totalDone count

    // Save training day

    // Clear workout presenter
  }

  fun getTrainingDays(): Observable<List<TrainingDay>> {
    return Observable.from(TrainingCategory.values())
        .map { repo.getTrainingDay(it) }
        .toList()
  }
}