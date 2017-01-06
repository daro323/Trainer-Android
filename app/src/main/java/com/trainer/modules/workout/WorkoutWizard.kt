package com.trainer.modules.workout

import com.trainer.modules.training.TrainingDay
import rx.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * Created by dariusz on 06/01/17.
 */
class WorkoutWizard @Inject constructor() {

  lateinit var trainingDay: TrainingDay
  val workoutEventsSubject = BehaviorSubject.create<WorkoutEvent>()

  fun onWorkoutEvent() = workoutEventsSubject.asObservable()

  fun getWorkoutList() = trainingDay.workout.series
}