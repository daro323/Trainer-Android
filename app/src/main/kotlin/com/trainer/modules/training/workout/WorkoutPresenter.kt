package com.trainer.modules.training.workout

import com.trainer.R
import com.trainer.modules.countdown.CountDownNotification
import com.trainer.modules.training.workout.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.modules.training.workout.ProgressStatus.COMPLETE
import com.trainer.modules.training.workout.ProgressStatus.STARTED
import com.trainer.modules.training.workout.WorkoutEvent.*
import com.trainer.modules.training.workout.WorkoutPresenterHelper.HelperCallback
import com.trainer.persistence.training.TrainingPlanDao
import com.trainer.persistence.training.TrainingRepository
import com.trainer.ui.training.rest.RestActivity
import io.reactivex.processors.BehaviorProcessor
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

  lateinit var trainingDay: TrainingPlanDao.TrainingDay
  private val workoutEventsProcessor = BehaviorProcessor.create<WorkoutEvent>()
  private lateinit var helper: WorkoutPresenterHelper
  private var currentSerieIdx: Int = VALUE_NOT_SET

  fun getHelper() = helper

  fun onWorkoutEvent() = workoutEventsProcessor.toObservable()

  fun getWorkoutList() = trainingDay.workout.series

  fun getWorkoutStatus() = trainingDay.workout.status()

  fun getWorkoutTitle() = trainingDay.category

  fun getWorkoutCategory() = trainingDay.category

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
    if (restManager.isResting()) restManager.abortRest()
    helper.onSkipSerie()
    repo.saveTrainingPlan()
    workoutEventsProcessor.onNext(SERIE_COMPLETED)
  }

  fun serieCompleteHandled() {
    if (getWorkoutStatus() == COMPLETE) workoutEventsProcessor.onNext(WORKOUT_COMPLETED)
    else workoutEventsProcessor.onNext(ONGOING)
  }

  override fun onSaveSerie(serie: Serie, skipRest: Boolean) {
    trainingDay.workout.series.indexOf(serie).apply {
      trainingDay.workout.series[this] = serie
    }
    repo.saveTrainingDay(trainingDay)

    val restTime = helper.getRestTime()

    if (skipRest.not() && getWorkoutStatus() != COMPLETE && restTime > 0) workoutEventsProcessor.onNext(WorkoutEvent.REST)
    else determineNextStep()
  }

  override fun hasOtherSerieStarted(thanSerie: Serie) = getWorkoutList()
      .filter { it != thanSerie }
      .any { it.status() == STARTED }

  fun onStartRest() {
    restManager.startRest(getRestTime(),
        notificationData = CountDownNotification.InitData(R.string.rest_in_progress_notification_title, RestActivity::class.java.name, CountDownNotification.DUMMY_REQUESTCODE, CountDownNotification.REST_NOTIFICATION_ID),
        withVibration = true)
  }

  fun onAbortRest() {
    restManager.abortRest()
    determineNextStep()
  }

  fun restComplete() {
    restManager.onRestComplete()
    determineNextStep()
  }

  fun onRestEvents() = restManager.getRestEvents()

  fun getRestTime() = helper.getRestTime()

  private fun determineNextStep() {
    if (getWorkoutStatus() == COMPLETE) {
      workoutEventsProcessor.onNext(WORKOUT_COMPLETED)
    } else if (helper.getSerie().status() == COMPLETE) {
      workoutEventsProcessor.onNext(SERIE_COMPLETED)
    } else {
      workoutEventsProcessor.onNext(ONGOING)
      helper.determineNextStep()
    }
  }
}