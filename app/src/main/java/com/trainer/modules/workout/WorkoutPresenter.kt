package com.trainer.modules.workout

import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.TrainingDay
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import rx.subjects.PublishSubject
import javax.inject.Inject

/**
 * Operates on a training day.
 * Created by dariusz on 06/01/17.
 */
class WorkoutPresenter @Inject constructor() {

  lateinit var trainingDay: TrainingDay

  val workoutEventsSubject = PublishSubject.create<WorkoutEvent>()

  private var currentSerieIdx: Int = -1
  private var currentSetIdx: Int = -1

  fun onWorkoutEvent() = workoutEventsSubject.asObservable()

  fun getWorkoutList() = trainingDay.workout.series

  fun isWorkoutComplete() = trainingDay.workout.isComplete()

  fun getWorkoutTitle() = trainingDay.category.name

  fun getCurrentSerie() = trainingDay.workout.series[currentSerieIdx]

  fun getSet(setId: String) = trainingDay.workout.series.find {
    when (it) {
      is Set -> it.id() == setId
      is SuperSet -> it.setList.any { it.id() == setId }
      else -> throw IllegalArgumentException("Unsupported serie type= ${it.javaClass}")
    }
  }?.run { if (this is SuperSet) setList.find { it.id() == setId } else this } as Set? ?: throw IllegalArgumentException("No set found for ID= $setId")

  fun selectSerie(index: Int) {
    currentSerieIdx = index
  }

  fun saveSetResult(weight: Float, rep: Int) {
    if (weight < 0 && getCurrentSet().exercise.weightType != BODY_WEIGHT) throw IllegalArgumentException("Missing weight value! It's expected in saveSetResult for weight type= ${getCurrentSet().exercise.weightType}")
    // TODO
    // Add repetition to current set's progress

    // Update currentSetIdx to next unfinished Set in this Serie (if this is just Set then ignore)

    // Fire event (rest or next Set or complete)
  }

  private fun getCurrentSet(): Set {
    val currentSerie = getCurrentSerie()
    if (currentSerie is SuperSet && currentSetIdx == -1) throw IllegalStateException("current Set idx was not set for current SuperSet!")

    return when (currentSerie) {
      is Set -> currentSerie
      is SuperSet -> currentSerie.setList[currentSetIdx]
      else -> throw IllegalArgumentException("Can't getCurrentSet for unsupported serie type= ${currentSerie.javaClass}")
    }
  }
}