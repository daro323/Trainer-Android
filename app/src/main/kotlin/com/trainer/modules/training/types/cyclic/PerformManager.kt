package com.trainer.modules.training.types.cyclic

import android.content.Context
import com.trainer.commons.Lg
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.countdown.CountDownNotificationProvider.Type.PERFORMING
import com.trainer.modules.countdown.CountDownReceiver
import com.trainer.modules.countdown.CountDownService
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Created by dariusz on 21/03/17.
 */
@ApplicationScope
class PerformManager @Inject constructor(@ForApplication val context: Context) {

  private var countDownReceiver: CountDownReceiver? = null
  private val performEventsProcessor = BehaviorProcessor.create<Int>()

  fun startPerforming(initValue: Int) {
    Lg.d("startPerforming")
    countDownReceiver = CountDownService.start(initValue, PERFORMING, context) { performEventsProcessor.onNext(it) }
        .apply { register(context) }
  }

  fun abortPerforming() {
    Lg.d("abortPerforming")
    require(countDownReceiver != null) { "Attempt to abort PerformManager count down when there was no receiver registered!" }
    CountDownService.abort(context)
    finish()
  }

  fun onPerformingComplete() {
    Lg.d("onPerformingComplete")
    require(countDownReceiver != null) { "Attempt to complete PerformManager count down when there was no receiver registered!" }
    CountDownService.finish(context)
    finish()
  }

  fun getPerformEvents() = performEventsProcessor.toObservable()

  private fun finish() {
    countDownReceiver!!.unregister(context)
    countDownReceiver = null
    performEventsProcessor.onNext(-1)
  }
}