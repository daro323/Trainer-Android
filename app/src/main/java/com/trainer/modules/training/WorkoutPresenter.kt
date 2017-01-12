package com.trainer.modules.training

import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.WorkoutEvent.REST
import com.trainer.modules.training.WorkoutEvent.SERIE_COMPLETED
import rx.subjects.PublishSubject
import javax.inject.Inject
import kotlin.comparisons.compareBy

/**
 * Operates on a training day.
 * Created by dariusz on 06/01/17.
 */
class WorkoutPresenter @Inject constructor() {

  companion object {
    val WEIGHT_NA_VALUE = -1f   // value for weight which is considered not applicable
  }

  lateinit var trainingDay: TrainingDay

  val workoutEventsSubject = PublishSubject.create<WorkoutEvent>()

  private var currentSerieIdx: Int = -1
  private var currentSetIdx: Int = -1

  fun onWorkoutEvent() = workoutEventsSubject.asObservable()

  fun getWorkoutList() = trainingDay.workout.series

  fun isWorkoutComplete() = trainingDay.workout.isComplete()

  fun getWorkoutTitle() = trainingDay.category.name

  fun getRestTime() = getCurrentSet().restTimeSec

  fun getCurrentSerie(): Series {
    require(currentSerieIdx != -1) { "Current serie idx is not set!" }
    return trainingDay.workout.series[currentSerieIdx]
  }

  fun getSet(setId: String) = trainingDay.workout.series.find {
    when (it) {
      is Set -> it.id() == setId
      is SuperSet -> it.setList.any { it.id() == setId }
      else -> throw IllegalArgumentException("Unsupported serie type= ${it.javaClass}")
    }
  }?.run { if (this is SuperSet) setList.find { it.id() == setId } else this } as Set? ?: throw IllegalArgumentException("No set found for ID= $setId")

  fun getCurrentSet(): Set {
    val currentSerie = getCurrentSerie()
    if (currentSerie is SuperSet && currentSetIdx == -1) throw IllegalStateException("current Set idx was not set for current SuperSet!")

    return when (currentSerie) {
      is Set -> currentSerie
      is SuperSet -> if (currentSerie.isComplete()) currentSerie.setList[0] else currentSerie.setList[currentSetIdx]  // show first set if serie completed
      else -> throw IllegalArgumentException("Can't getCurrentSet for unsupported serie type= ${currentSerie.javaClass}")
    }
  }

  fun selectSerie(index: Int) {
    currentSerieIdx = index
    if (getCurrentSerie().isComplete().not()) refreshCurrentSetIdx()
  }

  fun saveSetResult(weight: Float, rep: Int) {
    val currentSet = getCurrentSet()
    if (weight < 0 && currentSet.exercise.weightType != BODY_WEIGHT) throw IllegalArgumentException("Missing weight value! It's expected in saveSetResult for weight type= ${getCurrentSet().exercise.weightType}")

    currentSet.progress.add(Repetition(weight, rep, getCurrentWeightType()))

    val restTime = currentSet.restTimeSec
    if (restTime > 0) workoutEventsSubject.onNext(REST) else determineNextStep()
  }

  fun skipSerie() {
    val currentSerie = getCurrentSerie()
    if (currentSerie.isComplete().not()) {
      when(currentSerie) {
        is Set -> skipSet(currentSerie)
        is SuperSet -> currentSerie.setList.forEach { skipSet(it) }
        else -> throw IllegalStateException("Can't skipSerie for unsupported serie type= ${currentSerie.javaClass}")
      }
    }
    workoutEventsSubject.onNext(SERIE_COMPLETED)
  }

  fun isCurrentSet(set: Set) = if (getCurrentSerie().isComplete()) false else getCurrentSet() == set  // TODO: Handle requests from other series

  fun restComplete() {
    determineNextStep()
  }

  private fun skipSet(set: Set) {
    while (set.progress.size < set.seriesCount) {
      set.progress.add(zeroRepetition(set))
    }
  }

  /**
   * Updates currentSetIdx in current Serie to next unfinished Set with the least progress.
   * If this is just Set then ignore.
   */
  private fun refreshCurrentSetIdx() {
    val currentSerie = getCurrentSerie()
    currentSetIdx = when (currentSerie) {
      is Set -> -1
      is SuperSet -> currentSerie.setList
          .filter { it.isComplete().not() }
          .sortedWith(compareBy { it.progress.size })
          .first()
          .run {
            currentSerie.setList.indexOf(this)
          }
      else -> throw IllegalStateException("Can't refreshCurrentSetIdx for unsupported serie type= ${currentSerie.javaClass}")
    }
  }

  private fun determineNextStep() {
    if (getCurrentSerie().isComplete()) {
      workoutEventsSubject.onNext(SERIE_COMPLETED)
    } else {
      refreshCurrentSetIdx()
      workoutEventsSubject.onNext(WorkoutEvent.DO_NEXT_SET)
    }
  }

  private fun zeroRepetition(forSet: Set): Repetition {
    val weightType = forSet.exercise.weightType
    return Repetition(if (weightType == BODY_WEIGHT) WEIGHT_NA_VALUE else 0f, 0, weightType)
  }

  private fun getCurrentWeightType() = getCurrentSet().exercise.weightType
}