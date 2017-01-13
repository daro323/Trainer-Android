package com.trainer.ui

import android.os.Bundle
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.extra
import com.trainer.utils.bindView
import io.netopen.hotbitmapgg.library.view.RingProgressBar
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RestActivity : BaseActivity(R.layout.activity_rest) {

  @Inject lateinit var vibrator: Vibrator
  private val restTimeSec: Int by extra(EXTRA_REST_TIME_SEC)
  private val progressView: RingProgressBar by bindView(R.id.progress_view)
  private val countDownText: TextView by bindView(R.id.countdown_text)
  private val skipButton: Button by bindView(R.id.skip_rest_btn)
  private var timerSubscription: Subscription = Subscriptions.unsubscribed()

  private var isTimerFinished: Boolean = false   // State
  private var isActivityResumed: Boolean = false   // State

  companion object {
    val EXTRA_REST_TIME_SEC = "EXTRA_REST_TIME_SEC"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    require(restTimeSec > 0) { "RestActivity invoked for a not set restTime value" }

    when(isTimerFinished) {
      true -> finish()
      false -> initialize()
    }
  }

  override fun onResume() {
    super.onResume()
    isActivityResumed = true
  }

  override fun onPause() {
    isActivityResumed = false
    super.onPause()
  }

  override fun onDestroy() {
    timerSubscription.unsubscribe()
    super.onDestroy()
  }

  override fun onBackPressed() {
    // Block back button (just via skip can exit)
  }

  private fun initialize() {
    if (timerSubscription.isUnsubscribed) {
      skipButton.setOnClickListener { finish() }
      progressView.max = restTimeSec
      updateCountDown(restTimeSec)
      subscribeForTimer()
    }
  }

  private fun updateCountDown(countDown: Int) {
    progressView.progress = countDown
    countDownText.text = String.format(getString(R.string.countdown_text), countDown)
  }

  private fun subscribeForTimer() {
    timerSubscription = Observable.interval(1, TimeUnit.SECONDS)
        .map { (restTimeSec - it).toInt() }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { isTimerFinished = true }
        .subscribe {
          updateCountDown(it)
          if (it == 0) {
            vibrator.vibrate(1000)
            timerSubscription.unsubscribe()
            if (isActivityResumed) finish()
          }
        }
  }
}
