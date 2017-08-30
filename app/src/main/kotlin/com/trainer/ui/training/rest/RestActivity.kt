package com.trainer.ui.training.rest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.commons.Lg
import com.trainer.commons.typedviewholder.TypedViewHolderAdapter
import com.trainer.commons.typedviewholder.registerHolder
import com.trainer.modules.training.workout.WorkoutManager
import com.trainer.modules.training.workout.WorkoutPresenter
import com.trainer.modules.training.workout.ProgressStatus
import com.trainer.extensions.getColorFromRes
import com.trainer.extensions.ioMain
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_rest.*
import kotlinx.android.synthetic.main.item_progress.view.*
import javax.inject.Inject

class RestActivity : BaseActivity(R.layout.activity_rest) {

  @Inject lateinit var workoutManager: WorkoutManager

  private val presenter: WorkoutPresenter by lazy { workoutManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private var restSubscription = Disposables.disposed()
  private val typedAdapter = TypedViewHolderAdapter.Builder<Any>().apply {
    registerHolder(R.layout.item_progress) { model: ProgressItem ->
      itemView.apply {
        uiProgressItemName.text = model.name
        uiProgressItemName.setTextColor(
            if (model.isCurrent) getColorFromRes(R.color.colorAccent) else getColorFromRes(R.color.black)
        )
      }
    }
  }.build()

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
    showProgressList()
    subscribeForRest()
  }

  private fun showProgressList() {
    presenter.getWorkoutList()
        .filter { it.status() != ProgressStatus.COMPLETE }
        .map { ProgressItem(it.name(), it.status() == ProgressStatus.STARTED) }
        .run { typedAdapter.data = this }

    uiProgressList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    uiProgressList.adapter = typedAdapter
  }

  private fun onRestEvent(countDown: Int) {
    if (countDown > 0) {
      updateCountDown(countDown)
      container.visibility = VISIBLE
    } else {
      Lg.d("Finished - closing...")
      updateCountDown(countDown)
      restSubscription.dispose()
      presenter.restComplete()
      finish()
    }
  }

  private fun updateCountDown(countDown: Int) {
    progressView.progress = countDown
    countDownText.text = String.format(getString(R.string.countdown_text_with_abort), countDown)
  }

  private fun subscribeForRest() {
    restSubscription.dispose()
    restSubscription = presenter.onRestEvents()
        .filter { it >= 0 }
        .ioMain()
        .doOnSubscribe { progressView.max = presenter.getRestTime() }
        .subscribe { onRestEvent(it) }
  }
}
