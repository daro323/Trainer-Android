package com.trainer.modules.training.rest

import android.content.Context
import android.os.Vibrator
import com.trainer.commons.Lg
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.countdown.CountDownNotificationProvider.Type.RESTING
import com.trainer.modules.countdown.CountDownReceiver
import com.trainer.modules.countdown.CountDownService
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * This class needs to be explicitly started/aborted/finished in order to correctly handle lifecycle of ForegroundService cooperator
 * Created by dariusz on 18/01/17.
 */
@ApplicationScope
class RestManager @Inject constructor(val vibrator: Vibrator,
                                      @ForApplication val context: Context) {

  companion object {
    private const val VIBRATE_DURATION_MS = 1200L
  }

  private var countDownReceiver: CountDownReceiver? = null
  private val restEventsProcessor = BehaviorProcessor.create<Int>()

  fun startRest(initialStartValue: Int, withVibration: Boolean = true) {
    Lg.d("startRest")
    countDownReceiver = CountDownService.start(initialStartValue, RESTING, context) {
      restEventsProcessor.onNext(it)
      if (it == 0) onCountDownFinished(withVibration)
    }.apply { register(context) }
  }

  fun abortRest() {
    Lg.d("abortRest")
    require(countDownReceiver != null) { "Attempt to abort RestManager count down when there was no receiver registered!" }
    CountDownService.abort(context)
    finish()
  }

  fun onRestComplete() {
    Lg.d("onRestComplete")
    require(countDownReceiver != null) { "Attempt to complete RestManager count down when there was no receiver registered!" }
    CountDownService.finish(context)
    finish()
  }

  fun getRestEvents() = restEventsProcessor.toObservable()

  fun isResting() = countDownReceiver != null

  private fun onCountDownFinished(vibrationActive: Boolean) {
//    if (vibrationActive) vibrator.vibrate(VIBRATE_DURATION_MS)
  }

  private fun finish() {
    countDownReceiver!!.unregister(context)
    countDownReceiver = null
    restEventsProcessor.onNext(-1)
  }
}