package com.trainer.modules.rest

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.rest.RestState.*
import com.trainer.modules.training.TrainingRepository
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by dariusz on 18/01/17.
 */
@ApplicationScope
class RestManager @Inject constructor(val repo: TrainingRepository) {

  companion object {
    val IDLE_STATE_REST_EVENT = RestEvent(0, IDLE)
  }

  val restEventsSubject = BehaviorSubject.create<RestEvent>(IDLE_STATE_REST_EVENT)
  var restCountSubscription = Subscriptions.unsubscribed()

  fun startRest(initialStartValue: Int) {
    require(initialStartValue > 0) { "Start Rest invoked without rest time being set!" }

    val savedRestTime = repo.getRestTime()
    if (savedRestTime == 0) {
      startRestCount(0, initialStartValue)
    } else {
      val lastInterval = initialStartValue - savedRestTime
      startRestCount(lastInterval, initialStartValue)
    }
  }

  fun stopRest() {
    restCountSubscription.unsubscribe()
    repo.saveRestTime(0)
    restEventsSubject.onNext(IDLE_STATE_REST_EVENT)
  }

  fun getRestEvents() = restEventsSubject.asObservable()

  private fun startRestCount(lastInterval: Int, totalRestTime: Int) {
    restCountSubscription = Observable.interval(1, TimeUnit.SECONDS)
        .startWith(0)
        .map { (totalRestTime - lastInterval - it).toInt() }
        .doOnNext { repo.saveRestTime(it) }
        .map {
          if (it > 0) RestEvent(it, COUNTDOWN)
          else if (it == 0) RestEvent(it, FINISHED)
          else throw IllegalStateException("")
        }
        .doOnNext { restEventsSubject.onNext(it) }
        .subscribe { if (it.state == FINISHED) restCountSubscription.unsubscribe() }
  }
}