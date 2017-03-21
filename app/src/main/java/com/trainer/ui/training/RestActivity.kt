package com.trainer.ui.training

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.commons.Lg
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.business.WorkoutPresenter
import com.trainer.extensions.ioMain
import com.trainer.modules.training.rest.RestEvent
import com.trainer.modules.training.rest.RestState.*
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_rest.*
import javax.inject.Inject

class RestActivity : BaseActivity(R.layout.activity_rest) {

  @Inject lateinit var trainingManager: TrainingManager

  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private var restSubscription = Disposables.disposed()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    Lg.d("onStart()")
    super.onStart()
    initialize()
  }

  override fun onBackPressed() {
    // Block back button (just via skip can exit)
  }

  override fun onStop() {
    restSubscription.dispose()
    container.visibility = INVISIBLE
    super.onStop()
  }

  private fun initialize() {
    countDownText.setOnClickListener {
      restSubscription.dispose()
      presenter.onAbortRest()
      finish()
    }
    subscribeForRest()
  }

  private fun onRestEvent(event: RestEvent) {
    when (event.state) {

      START, RESTING -> {
        updateCountDown(event.countDown)
        container.visibility = VISIBLE
      }

      FINISHED -> {
        Lg.d("Finished - closing...")
        updateCountDown(event.countDown)
        restSubscription.dispose()
        presenter.restComplete()
        finish()
      }

      else -> {}  //ignore
    }
  }

  private fun updateCountDown(countDown: Int) {
    progressView.progress = countDown
    countDownText.text = String.format(getString(R.string.countdown_text_with_abort), countDown)
  }

  private fun subscribeForRest() {
    restSubscription.dispose()
    restSubscription = presenter.onRestEvents()
        .ioMain()
        .doOnSubscribe { progressView.max = presenter.getRestTime() }
        .subscribe { onRestEvent(it) }
  }
}
