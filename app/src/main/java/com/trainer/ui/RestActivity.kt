package com.trainer.ui

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.modules.rest.RestEvent
import com.trainer.modules.rest.RestService
import com.trainer.modules.rest.RestState.*
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import kotlinx.android.synthetic.main.activity_rest.*
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class RestActivity : BaseActivity(R.layout.activity_rest) {

  @Inject lateinit var trainingManager: TrainingManager

  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
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
    container.visibility = INVISIBLE
    super.onStop()
  }

  private fun initialize() {
    countDownText.setOnClickListener { close() }
    subscribeForRest()
  }

  private fun onRestEvent(event: RestEvent) {
    when(event.state) {

      IDLE -> RestService.startRest(this)

      COUNTDOWN -> {
        updateCountDown(event.countDown)
        container.visibility = VISIBLE
      }

      FINISHED -> {
        updateCountDown(event.countDown)
        close()
      }
    }
  }

  private fun close() {
    restSubscription.unsubscribe()
    RestService.stopRest(this)
    finish()
  }

  private fun updateCountDown(countDown: Int) {
    progressView.progress = countDown
    countDownText.text = String.format(getString(R.string.countdown_text), countDown)
  }

  private fun subscribeForRest() {
    restSubscription.unsubscribe()
    restSubscription = presenter.getRestEvents()
        .ioMain()
        .doOnSubscribe { progressView.max = presenter.getRestTime() }
        .subscribe { onRestEvent(it) }
  }
}
