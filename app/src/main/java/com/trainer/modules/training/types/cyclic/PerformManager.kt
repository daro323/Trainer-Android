package com.trainer.modules.training.types.cyclic

import android.content.Context
import com.trainer.commons.Lg
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.ioMain
import com.trainer.modules.countdown.CountDownEvent
import com.trainer.modules.countdown.CountDownService
import com.trainer.modules.countdown.CountDownServiceClient
import com.trainer.modules.countdown.CountDownState
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Created by dariusz on 21/03/17.
 */
@ApplicationScope
class PerformManager @Inject constructor(val performNotificationManager: PerformNotificationManager,
                                         @ForApplication val context: Context) : CountDownServiceClient {

  private var countDownDisposable = Disposables.disposed()
  private val performEventsProcessor = BehaviorProcessor.create<CountDownEvent>()

  fun startPerforming(initValue: Int) {
    Lg.d("startPerforming")
    CountDownService.start(initValue, this, context)
  }

  fun abortPerforming() {
    Lg.d("abortPerforming")
    countDownDisposable.dispose()
    CountDownService.abort(context)
    performEventsProcessor.onNext(CountDownEvent(0, CountDownState.FINISHED))
  }

  fun onPerformingComplete() {
    Lg.d("onPerformingComplete")
    countDownDisposable.dispose()
    CountDownService.finish(context)
  }

  override fun onCountDownServiceReady(service: CountDownService) {
    countDownDisposable.dispose()
    countDownDisposable = service.onCountDownEvents()
        .ioMain()
        .doOnSubscribe { performNotificationManager.showNotification(service) }
        .doOnDispose { performNotificationManager.hideNotification() }
        .subscribe {
          performNotificationManager.updateNotification(it.countDown, service)
          performEventsProcessor.onNext(it)
        }
  }

  fun getPerformEvents() = performEventsProcessor.toObservable()
}