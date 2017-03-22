package com.trainer.ui.training.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.extensions.inflate
import com.trainer.modules.training.types.cyclic.CycleState
import com.trainer.modules.training.types.cyclic.CycleState.*
import com.trainer.ui.training.cyclic.BodyViewModel
import com.trainer.ui.training.cyclic.CycleViewCallback
import com.trainer.ui.training.cyclic.CycleViewEvent
import io.netopen.hotbitmapgg.library.view.RingProgressBar

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewBody: FrameLayout {

  private lateinit var startBtn: Button
  private lateinit var doMoreBtn: Button
  private lateinit var getReadyCountDown: TextView
  private lateinit var progressContainer: FrameLayout
  private lateinit var progressView: RingProgressBar
  private lateinit var exerciseCountDown: TextView

  private lateinit var callback: CycleViewCallback

  constructor(context: Context) : super(context) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    inflateLayout()
  }

  fun bindViewModel(callback: CycleViewCallback) {
    this.callback = callback.apply { getViewModelChanges().subscribe { updateUI(it.state, it.bodyViewModel) } }
  }

  private fun updateUI(state: CycleState, bodyViewModel: BodyViewModel) {
    when (state) {
      NEW -> {
        startBtn.visibility = VISIBLE
        doMoreBtn.visibility = GONE
        getReadyCountDown.visibility = GONE
        progressContainer.visibility = GONE
      }

      GET_READY -> {
        startBtn.visibility = GONE
        doMoreBtn.visibility = GONE
        progressContainer.visibility = GONE
        getReadyCountDown.apply {
          visibility = VISIBLE
          text = bodyViewModel.countDown.toString()
        }
      }

      PERFORMING, RESTING -> {
        startBtn.visibility = GONE
        doMoreBtn.visibility = GONE
        getReadyCountDown.visibility = GONE
        progressContainer.visibility = VISIBLE
        progressView.apply {
          max = bodyViewModel.totalCountDown
          progress = bodyViewModel.countDown
        }
        exerciseCountDown.text = String.format(context.getString(R.string.countdown_text), bodyViewModel.countDown)
      }

      DONE -> {
        // TODO
      }

      COMPLETE -> {
        // TODO
      }
    }
  }

  private fun inflateLayout() {
    inflate(R.layout.cycle_view_body, this, true).apply {
      startBtn = (findViewById(R.id.start_button) as Button).apply { setOnClickListener { callback.onViewEvent(CycleViewEvent.START) } }
      doMoreBtn = findViewById(R.id.do_more_button) as Button
      getReadyCountDown = findViewById(R.id.get_ready_countdown) as TextView
      progressContainer = findViewById(R.id.progress_container) as FrameLayout
      progressView = findViewById(R.id.progress_view) as RingProgressBar
      exerciseCountDown = findViewById(R.id.exercise_countdown) as TextView
    }
  }
}