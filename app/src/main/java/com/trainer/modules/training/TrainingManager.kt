package com.trainer.modules.training

import com.trainer.modules.workout.WorkoutWizard
import rx.Observable
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by dariusz on 06/01/17.
 */
class TrainingManager @Inject constructor(val repo: TrainingRepository,
                                          val workoutWizardProvider: Provider<WorkoutWizard>) {

  private var workoutWizard: WorkoutWizard? = null  // state

  fun getAllTrainingDaysData(): Observable<List<TrainingDay>> {
    return Observable.from(TrainingCategory.values())
        .map { repo.getTrainingDay(it) }
        .toList()
  }

  fun startWorkout(forCategory: TrainingCategory) {
    require(isWorkoutStarted().not()) { "Can't start new workout - there is one already started!" }
    workoutWizard = workoutWizardProvider
        .get()
        .apply { trainingDay = repo.getTrainingDay(forCategory) }
  }

  fun isWorkoutStarted() = workoutWizard != null

  fun getCurrentWorkout(): WorkoutWizard? {
    return workoutWizard
  }
}