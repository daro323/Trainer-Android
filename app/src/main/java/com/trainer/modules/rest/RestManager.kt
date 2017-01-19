package com.trainer.modules.rest

import com.trainer.modules.rest.RestEventType.*
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.TrainingRepository
import com.trainer.modules.training.WorkoutPresenter
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by dariusz on 18/01/17.
 */
class RestManager @Inject constructor(val repo: TrainingRepository,
                                      val trainingManager: TrainingManager) {

  private var shouldStop: Boolean = false
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // can call this only after component.inject()!

  fun startRest(): Observable<RestEvent> {
    val restTime = presenter.getRestTime()
    require(repo.getRestTime() == 0) { "startRest invoked but there is still some rest time to be done! (should call continue rest), saved rest time= ${repo.getRestTime()}" }
    require(restTime > 0) { "Start test invoked without rest time set!" }

    shouldStop = false
    return getRestObservable(0, restTime)
        .startWith(RestEvent(restTime, restTime, STARTED))
  }

  fun continueRest(): Observable<RestEvent> {
    val savedRestTime = repo.getRestTime()
    require(savedRestTime > 0) { "Attempt to continue rest when there is no saved rest time!" }
    val totalRestTime = presenter.getRestTime()
    val lastInterval = totalRestTime - savedRestTime

    shouldStop = false
    return getRestObservable(lastInterval, totalRestTime)
        .startWith(RestEvent(totalRestTime, savedRestTime, STARTED))
  }

  fun stopRest() {
    shouldStop = true
  }

  private fun getRestObservable(lastInterval: Int, totalRestTime: Int) = Observable.interval(1, TimeUnit.SECONDS)
      .map { (totalRestTime - lastInterval - it).toInt() }
      .doOnNext { repo.saveRestTime(it) }
      .map {
        if (shouldStop) {
          repo.saveRestTime(0)
          RestEvent(totalRestTime, it, ABORTED)
        } else {
          if (it > 0) RestEvent(totalRestTime, it, COUNTDOWN)
          else if (it == 0) RestEvent(totalRestTime, it, FINISHED)
          else throw IllegalStateException("")
        }
      }
}