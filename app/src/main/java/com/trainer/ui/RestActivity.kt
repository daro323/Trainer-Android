package com.trainer.ui

import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.modules.rest.RestEvent
import com.trainer.modules.rest.RestEventType.*
import com.trainer.modules.rest.RestService
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.utils.bindView
import io.netopen.hotbitmapgg.library.view.RingProgressBar
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class RestActivity : BaseActivity(R.layout.activity_rest) {

  @Inject lateinit var trainingManager: TrainingManager

  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private val progressView: RingProgressBar by bindView(R.id.progress_view)
  private val countDownText: TextView by bindView(R.id.countdown_text)
  private val container: FrameLayout by bindView(R.id.rest_container)
  private var restSubscription: Subscription = Subscriptions.unsubscribed()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    initialize()
  }

  override fun onBackPressed() {
    // Block back button (just via skip can exit)
  }

  override fun onStop() {
    restSubscription.unsubscribe()
    super.onStop()
  }

  private fun initialize() {
    countDownText.setOnClickListener { RestService.stopRest(this) }
    subscribeForRest()
  }

  private fun onRestEvent(event: RestEvent) {
    when(event.type) {
      STARTED -> event.run {
        progressView.max = startValue
        updateCountDown(countDown)
        container.visibility = VISIBLE
      }

      COUNTDOWN -> updateCountDown(event.countDown)

      FINISHED, ABORTED -> {
        updateCountDown(event.countDown)
        finish()
      }
    }
  }

  private fun updateCountDown(countDown: Int) {
    progressView.progress = countDown
    countDownText.text = String.format(getString(R.string.countdown_text), countDown)
  }

  // TODO: Fix when to start the service and when to contiune!
  private fun subscribeForRest() {
    restSubscription.unsubscribe()
    RestService.startRest(this)
    restSubscription = presenter.onRestTimeUpdateEvent()
        .ioMain()
        .subscribe { onRestEvent(it) }
  }
}
