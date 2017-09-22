package com.trainer.modules.training.workout.types.cyclic

import android.app.Application
import com.trainer.commons.Lg
import com.trainer.modules.countdown.CountDownNotification
import com.trainer.modules.countdown.CountDownReceiver
import com.trainer.modules.countdown.CountDownService
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dariusz on 21/03/17.
 */
@Singleton
class PerformManager @Inject constructor(val context: Application) {

  private var countDownReceiver: CountDownReceiver? = null
  private val performEventsProcessor = BehaviorProcessor.create<Int>()

  fun startPerforming(initValue: Int, notificationData: CountDownNotification.InitData) {
    Lg.d("startPerforming")
    countDownReceiver = CountDownService.start(initValue, notificationData, context) { performEventsProcessor.onNext(it) }
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

  fun isPerforming() = countDownReceiver != null

  private fun finish() {
    countDownReceiver!!.unregister(context)
    countDownReceiver = null
    performEventsProcessor.onNext(-1)
  }
}