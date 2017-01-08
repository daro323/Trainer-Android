package com.trainer.ui

import android.os.Bundle
import android.os.Vibrator
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import com.eralp.circleprogressview.CircleProgressView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.extra
import com.trainer.utils.bindView
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RestActivity : BaseActivity(R.layout.activity_rest) {

  @Inject lateinit var vibrator: Vibrator
  private val restTimeSec: Int by extra(EXTRA_REST_TIME_SEC)
  private val progressView: CircleProgressView by bindView(R.id.circle_progress_view)
  private val skipButton: Button by bindView(R.id.skip_rest_btn)
  private var timerSubscription: Subscription = Subscriptions.unsubscribed()

  companion object {
    val EXTRA_REST_TIME_SEC = "EXTRA_REST_TIME_SEC"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    if (timerSubscription.isUnsubscribed) {
      progressView.apply {
        interpolator = AccelerateDecelerateInterpolator()
        progress = 100f
      }
      skipButton.setOnClickListener { finish() }
      subscribeForTimer()
    }
  }

  override fun onDestroy() {
    timerSubscription.unsubscribe()
    super.onDestroy()
  }

  override fun onBackPressed() {
    // Block back button (just via skip can exit)
  }

  private fun subscribeForTimer() {
    timerSubscription = Observable.interval(1, TimeUnit.SECONDS)
        .map { (((restTimeSec - it) * 100) / restTimeSec).toFloat() }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          progressView.progress = it
          if (it == 0f) {
            vibrator.vibrate(800)
            timerSubscription.unsubscribe()
            finish()
          }
        }
  }
}
