package com.trainer.modules.countdown

import com.trainer.modules.countdown.CountDownState.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit

/**
 * Created by dariusz on 19/03/17.
 */
class CountDownTimer {

  private var countDownEventsProcessor = BehaviorProcessor.createDefault(IDLE_STATE_EVENT)
  private var countDownDisposable = Disposables.disposed()

  companion object {
    val IDLE_STATE_EVENT = CountDownEvent(0, IDLE)
  }

  fun start(startValue: Int) {
    require(startValue > 0) { "Start dount down invoked with invalid start value= $startValue!" }
    require(countDownDisposable.isDisposed) { "Attempt to start counting down when count down is already ongoing!" }

    countDownDisposable = Observable.interval(1, TimeUnit.SECONDS)
        .startWith(0)
        .map { (startValue - it).toInt() }
        .map {
          if (it > 0) CountDownEvent(it, COUNTDOWN)
          else if (it == 0) CountDownEvent(it, FINISHED)
          else throw IllegalStateException("")
        }
        .doOnNext { countDownEventsProcessor.onNext(it) }
        .subscribe { if (it.state == FINISHED) countDownDisposable.dispose() }
  }

  fun abort() {
    countDownDisposable.dispose()
    countDownEventsProcessor.onNext(IDLE_STATE_EVENT)
  }

  fun onCountDownEvents() = countDownEventsProcessor.toObservable()
}