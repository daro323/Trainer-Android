package com.trainer.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.os.Vibrator
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
  private var timerSubscription: Subscription = Subscriptions.unsubscribed()

  private var isActivityResumed: Boolean = false
  private var stateIsRestFinished: Boolean = false

  companion object {
    val EXTRA_REST_TIME_SEC = "EXTRA_REST_TIME_SEC"
    val STATE_IS_REST_FINISHED = "STATE_IS_REST_FINISHED"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
    savedInstanceState?.apply { stateIsRestFinished = getBoolean(STATE_IS_REST_FINISHED) }
  }

  override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    super.onSaveInstanceState(outState, outPersistentState)
    outState?.apply { putBoolean(STATE_IS_REST_FINISHED, stateIsRestFinished) }
  }

  override fun onStart() {
    super.onStart()
    require(restTimeSec > 0) { "RestActivity invoked for a not set restTime value" }

    when(stateIsRestFinished) {
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
      countDownText.setOnClickListener { finish() }
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
        .subscribe {
          updateCountDown(it)
          if (it == 0) {
            stateIsRestFinished = true
            vibrator.vibrate(1000)
            timerSubscription.unsubscribe()
            if (isActivityResumed) finish()
          }
        }
  }
}
