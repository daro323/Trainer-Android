package com.trainer.modules.countdown

import com.trainer.commons.Lg
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit

/**
 * Created by dariusz on 19/03/17.
 */
class CountingDownTimer {

  private val countDownEventsProcessor = BehaviorProcessor.create<Int>()
  private var countDownDisposable = Disposables.disposed()

  fun start(startValue: Int): Observable<Int> {
    require(startValue > 0) { "Start dount down invoked with invalid start value= $startValue!" }
    require(countDownDisposable.isDisposed) { "Attempt to start counting down when count down is already ongoing!" }
    Lg.d("start")

    countDownDisposable = Observable.interval(1, TimeUnit.SECONDS)
        .startWith(0)
        .map { (startValue - it).toInt() }
        .doOnNext { countDownEventsProcessor.onNext(it) }
        .subscribe { if (it == 0) countDownDisposable.dispose() }

    return countDownEventsProcessor.toObservable()
  }

  fun abort() {
    Lg.d("abort")
    countDownDisposable.dispose()
    countDownEventsProcessor.onComplete()
  }
}