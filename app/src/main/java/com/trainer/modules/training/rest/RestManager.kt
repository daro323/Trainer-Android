package com.trainer.modules.training.rest

import android.content.Context
import android.os.Vibrator
import com.trainer.commons.Lg
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.ioMain
import com.trainer.modules.countdown.CountDownService
import com.trainer.modules.countdown.CountDownServiceClient
import com.trainer.modules.countdown.CountDownState
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Created by dariusz on 18/01/17.
 */
@ApplicationScope
class RestManager @Inject constructor(val restNotification: RestNotificationManager,
                                      val vibrator: Vibrator,
                                      @ForApplication val context: Context) : CountDownServiceClient {

  companion object {
    private const val VIBRATE_DURATION_MS = 1200L
  }

  private var countDownDisposable = Disposables.disposed()
  private val restEventsProcessor = BehaviorProcessor.create<RestEvent>()

  fun startRest(initialStartValue: Int) {
    Lg.d("startRest")
    CountDownService.start(initialStartValue, this, context)
  }

  fun abortRest() {
    Lg.d("abortRest")
    countDownDisposable.dispose()
    CountDownService.abort(context)
    restEventsProcessor.onNext(RestEvent(0, RestState.ABORTED))
  }

  fun onRestComplete() {
    Lg.d("onRestComplete")
    CountDownService.finish(context)
  }

  override fun onCountDownServiceReady(service: CountDownService) {
    countDownDisposable.dispose()
    countDownDisposable = service.onCountDownEvents()
        .map {
          when (it.state) {
            CountDownState.IDLE -> RestEvent(it.countDown, RestState.START)
            CountDownState.COUNTDOWN -> RestEvent(it.countDown, RestState.RESTING)
            CountDownState.FINISHED -> RestEvent(it.countDown, RestState.FINISHED)
          }
        }
        .ioMain()
        .doOnSubscribe { restNotification.showNotification(service) }
        .doOnDispose { restNotification.hideNotification() }
        .doOnNext {
          restNotification.updateNotification(it.countDown, service)
          restEventsProcessor.onNext(it)
        }
        .filter { it.state == RestState.FINISHED }
        .subscribe {
          vibrator.vibrate(VIBRATE_DURATION_MS)
          restNotification.showForRestFinished(service)
        }
  }

  fun getRestEvents() = restEventsProcessor.toObservable()
}