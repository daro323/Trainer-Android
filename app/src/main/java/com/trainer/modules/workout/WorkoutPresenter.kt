package com.trainer.modules.workout

import com.trainer.modules.training.TrainingDay
import rx.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by dariusz on 06/01/17.
 */
class WorkoutPresenter @Inject constructor() {

  lateinit var trainingDay: TrainingDay   // operates on training day
  val workoutEventsSubject = PublishSubject.create<WorkoutEvent>()
  private var currentSerieIdx: Int = -1

  fun onWorkoutEvent() = workoutEventsSubject.asObservable()

  fun getWorkoutList() = trainingDay.workout.series

  fun isWorkoutComplete() = trainingDay.workout.isComplete()

  fun getWorkoutTitle() = trainingDay.category.name

  fun getCurrentSerie() = trainingDay.workout.series[currentSerieIdx]

  fun selectSerie(index: Int) {
    currentSerieIdx = index
  }
}