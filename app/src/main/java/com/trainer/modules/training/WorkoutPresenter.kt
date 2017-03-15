package com.trainer.modules.training

import com.trainer.modules.rest.RestManager
import com.trainer.modules.training.coredata.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.modules.training.coredata.ProgressStatus.COMPLETE
import com.trainer.modules.training.coredata.Serie
import com.trainer.modules.training.coredata.TrainingDay
import com.trainer.modules.training.coredata.WorkoutEvent
import com.trainer.modules.training.coredata.WorkoutEvent.SERIE_COMPLETED
import com.trainer.modules.training.coredata.WorkoutEvent.WORKOUT_COMPLETED
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Operates on a training day. Handles Serie operations in general.
 * In the realization of Serie composite pattern - this class doesn't (and shouldn't) operate on leaf classes. It only knows of Serie objects.
 * Also handles passing resting input/output events.
 *
 * Created by dariusz on 06/01/17.
 */
class WorkoutPresenter @Inject constructor(val repo: TrainingRepository,
                                           val restManager: RestManager) {

  lateinit var trainingDay: TrainingDay
  val workoutEventsProcessor = BehaviorProcessor.create<WorkoutEvent>()

  private var currentSerieIdx: Int = VALUE_NOT_SET

  fun onWorkoutEvent() = workoutEventsProcessor

  fun getWorkoutList() = trainingDay.workout.series

  fun getWorkoutStatus() = trainingDay.workout.status()

  fun getWorkoutTitle() = trainingDay.category.name

  fun getWorkoutCategory() = trainingDay.category

  fun getCurrentSerie(): Serie? {
    // TODO: Implement or remove if not needed
    return null
  }

  fun selectSerie(index: Int) {
    currentSerieIdx = index
    // TODO: Implement or remove if not needed
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

  fun onStartRest() {
    restManager.startRest(getRestTime())
  }

  fun onAbortRest() {
    restManager.abortRest()
  }

  fun restComplete() {
    // TODO: Inform current helper to determine next step
  }

  fun onRestEvent() = restManager.getRestEvents()

  fun getRestTime(): Int {
    // TODO: Delegate to helper which will pick current serie's step rest time
    return 0
  }
}