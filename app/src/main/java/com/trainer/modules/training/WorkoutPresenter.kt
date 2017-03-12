package com.trainer.modules.training

import com.trainer.modules.rest.RestManager
import com.trainer.modules.training.ProgressStatus.COMPLETE
import com.trainer.modules.training.ProgressStatus.STARTED
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.WorkoutEvent.*
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Operates on a training day.
 * Created by dariusz on 06/01/17.
 */
class WorkoutPresenter @Inject constructor(val repo: TrainingRepository,
                                           val restManager: RestManager) {

  companion object {
    const val WEIGHT_NA_VALUE = -1f   // value for weight which is considered not applicable
    const val NOT_SET_VALUE = -1
    const val DEFAULT_SET_INDEX = 0
  }

  lateinit var trainingDay: TrainingDay

  val workoutEventsProcessor = BehaviorProcessor.create<WorkoutEvent>()

  private var currentSerieIdx: Int = NOT_SET_VALUE
  private var currentSetIdx: Int = NOT_SET_VALUE

  fun onWorkoutEvent() = workoutEventsProcessor

  fun getWorkoutList() = trainingDay.workout.series

  fun getWorkoutStatus() = trainingDay.workout.status()

  fun getWorkoutTitle() = trainingDay.category.name

  fun getWorkoutCategory() = trainingDay.category

  fun getCurrentSerie(): Series {
    require(currentSerieIdx != NOT_SET_VALUE) { "Current serie idx is not set!" }
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
    if (currentSerie is SuperSet && currentSetIdx == NOT_SET_VALUE) throw IllegalStateException("current Set idx was not set for current SuperSet!")

    return when (currentSerie) {
      is Set -> currentSerie
      is SuperSet -> currentSerie.setList[currentSetIdx]
      else -> throw IllegalArgumentException("Can't getCurrentSet for unsupported serie type= ${currentSerie.javaClass}")
    }
  }

  fun selectSerie(index: Int) {
    currentSerieIdx = index
    if (getCurrentSerie().status() != COMPLETE) refreshCurrentSetIdx() else currentSetIdx = DEFAULT_SET_INDEX
  }

  fun saveSetResult(weight: Float, rep: Int) {
    val currentSet = getCurrentSet()
    if (weight < 0 && currentSet.exercise.weightType != BODY_WEIGHT) throw IllegalArgumentException("Missing weight value! It's expected in saveSetResult for weight type= ${getCurrentSet().exercise.weightType}")

    currentSet.progress.add(Repetition(weight, rep, getCurrentWeightType()))
    repo.saveTrainingPlan()

    val restTime = currentSet.restTimeSec
    if (restTime > 0) workoutEventsProcessor.onNext(REST) else determineNextStep()
  }

  fun skipSerie() {
    val currentSerie = getCurrentSerie()
    if (currentSerie.status() != COMPLETE) currentSerie.skipRemaining()
    repo.saveTrainingPlan()

    workoutEventsProcessor.onNext(SERIE_COMPLETED)
  }

  fun isCurrentSet(set: Set) = if (getCurrentSerie().status() == COMPLETE || hasOtherSerieStarted()) false else getCurrentSet() == set

  fun serieCompleteHandled() {
    if (getWorkoutStatus() == COMPLETE) {
      workoutEventsProcessor.onNext(WORKOUT_COMPLETED)
    } else {
      workoutEventsProcessor.onNext(WorkoutEvent.DO_NEXT_SET)
    }
  }

  fun onStartRest() {
    restManager.startRest(getRestTime())
  }

  fun onAbortRest() {
    restManager.abortRest()
  }

  fun restComplete() {
    determineNextStep()
  }

  fun getRestEvents() = restManager.getRestEvents()

  fun getRestTime() = getCurrentSet().restTimeSec

  private fun hasOtherSerieStarted() = getWorkoutList()
      .filter { it != getCurrentSerie() }
      .any { it.status() == STARTED }

  /**
   * Updates currentSetIdx in current Serie to next unfinished Set with the least progress.
   * If this is just Set then ignore.
   */
  private fun refreshCurrentSetIdx() {
    val currentSerie = getCurrentSerie()
    currentSetIdx = when (currentSerie) {
      is Set -> NOT_SET_VALUE
      is SuperSet -> currentSerie.setList
          .filter { it.status() != COMPLETE }
          .sortedBy { it.progress.size }
          .first()
          .run {
            currentSerie.setList.indexOf(this)
          }
      else -> throw IllegalStateException("Can't refreshCurrentSetIdx for unsupported serie type= ${currentSerie.javaClass}")
    }
  }

  private fun determineNextStep() {
    if (getWorkoutStatus() == COMPLETE) {
      workoutEventsProcessor.onNext(WORKOUT_COMPLETED)
    } else if (getCurrentSerie().status() == COMPLETE) {
      currentSetIdx = DEFAULT_SET_INDEX
      workoutEventsProcessor.onNext(SERIE_COMPLETED)
    } else {
      refreshCurrentSetIdx()
      workoutEventsProcessor.onNext(WorkoutEvent.DO_NEXT_SET)
    }
  }

  private fun getCurrentWeightType() = getCurrentSet().exercise.weightType
}