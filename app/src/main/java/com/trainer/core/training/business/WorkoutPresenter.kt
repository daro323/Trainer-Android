package com.trainer.core.training.business

import com.trainer.core.training.business.WorkoutPresenterHelper.HelperCallback
import com.trainer.core.training.model.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.core.training.model.ProgressStatus.COMPLETE
import com.trainer.core.training.model.ProgressStatus.STARTED
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.TrainingDay
import com.trainer.core.training.model.WorkoutEvent
import com.trainer.core.training.model.WorkoutEvent.SERIE_COMPLETED
import com.trainer.core.training.model.WorkoutEvent.WORKOUT_COMPLETED
import com.trainer.modules.training.rest.RestManager
import io.reactivex.Observable
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

/**
 * Operates on a training day. Handles Serie operations in general.
 * In the realization of Serie composite pattern - this class doesn't (and shouldn't) operate on leaf classes. It only knows of Serie interface.
 * Also handles passing resting input/output events.
 *
 * Created by dariusz on 06/01/17.
 */
class WorkoutPresenter @Inject constructor(val repo: TrainingRepository,
                                           val restManager: RestManager,
                                           val helperFactory: PresenterHelperFactory) : HelperCallback {

  lateinit var trainingDay: TrainingDay
  private val workoutEventsProcessor = PublishProcessor.create<WorkoutEvent>()
  private lateinit var helper: WorkoutPresenterHelper
  private var currentSerieIdx: Int = VALUE_NOT_SET

  fun getHelper() = helper

  fun onWorkoutEvent(): Observable<WorkoutEvent> = workoutEventsProcessor.toObservable()

  fun getWorkoutList() = trainingDay.workout.series

  fun getWorkoutStatus() = trainingDay.workout.status()

  fun getWorkoutTitle() = trainingDay.category

  fun getWorkoutCategory() = trainingDay.category

  fun getRestTime() = 8//helper.getRestTime()

  fun getCurrentSerie(): Serie {
    require(currentSerieIdx != VALUE_NOT_SET) { "Can't return current serie - it hasn't been selected yet!" }
    return getWorkoutList()[currentSerieIdx]
  }

  /**
   * Also automatically selects proper helper.
   */
  fun selectSerie(id: String) {
    val serie = trainingDay.workout.getSerie(id)?.apply {
      currentSerieIdx = trainingDay.workout.series.indexOf(this)
    } ?: throw IllegalArgumentException("Couldn't select serie of id= $id (doesn't exist in current workout)")

    helper = helperFactory.getHelperFor(serie, this)
  }

  fun skipSerie() {
    val currentSerie = getCurrentSerie()
    if (currentSerie.status() != COMPLETE) currentSerie.skipRemaining()
    repo.saveTrainingPlan()
    workoutEventsProcessor.onNext(SERIE_COMPLETED)
  }

  fun serieCompleteHandled() {
    if (getWorkoutStatus() == COMPLETE) {
      workoutEventsProcessor.onNext(WORKOUT_COMPLETED)
    } else {
      workoutEventsProcessor.onNext(WorkoutEvent.DO_NEXT)
    }
  }

  fun onStartRest() = restManager
      .apply { startRest(getRestTime()) }
      .run { getRestEvents() }

  fun onAbortRest() {
    restManager.abortRest()
  }

  fun restComplete() {
    workoutEventsProcessor.onNext(helper.determineNextStep(getWorkoutStatus()))
  }

  override fun onSaveSerie(serie: Serie) {
    trainingDay.workout.series.indexOf(serie).apply {
      trainingDay.workout.series[this] = serie
    }
    repo.saveTrainingDay(trainingDay)

    val restTime = helper.getRestTime()
    (if (getWorkoutStatus() != COMPLETE && restTime > 0) WorkoutEvent.REST else helper.determineNextStep(getWorkoutStatus()))
        .apply { workoutEventsProcessor.onNext(this) }
  }

  override fun hasOtherSerieStarted(thanSerie: Serie) = getWorkoutList()
      .filter { it != thanSerie }
      .any { it.status() == STARTED }
}