package com.trainer.modules.rest

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.rest.RestState.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by dariusz on 18/01/17.
 */
@ApplicationScope
class RestManager @Inject constructor() {

  companion object {
    val IDLE_STATE_REST_EVENT = RestEvent(0, IDLE)
  }

  var restEventsProcessor = BehaviorProcessor.createDefault(IDLE_STATE_REST_EVENT)
  var restCountSubscription = Disposables.disposed()

  fun startRest(initialStartValue: Int) {
    val value = 6
    require(initialStartValue > 0) { "Start Rest invoked without rest time being set!" }
    require(restCountSubscription.isDisposed) { "Attempt to start resting when resting is already ongoing!" }

    restCountSubscription = Observable.interval(1, TimeUnit.SECONDS)
        .startWith(0)
        .map { (value - it).toInt() }
        .map {
          if (it > 0) RestEvent(it, COUNTDOWN)
          else if (it == 0) RestEvent(it, FINISHED)
          else throw IllegalStateException("")
        }
        .doOnNext { restEventsProcessor.onNext(it) }
        .subscribe { if (it.state == FINISHED) restCountSubscription.dispose() }
  }

  fun abortRest() {
    restCountSubscription.dispose()
    restEventsProcessor.onNext(IDLE_STATE_REST_EVENT)
  }

  fun getRestEvents() = restEventsProcessor.toObservable()
}