package com.trainer.ui.training.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.extensions.inflate
import com.trainer.ui.training.cyclic.BodyViewModel
import com.trainer.ui.training.cyclic.CycleState
import com.trainer.ui.training.cyclic.CycleState.*
import com.trainer.ui.training.cyclic.CycleViewCallback
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
        // TODO
      }

      PERFORMING -> {
        // TODO
      }

      RESTING -> {
        // TODO
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
      startBtn = findViewById(R.id.start_button) as Button
      doMoreBtn = findViewById(R.id.do_more_button) as Button
      getReadyCountDown = findViewById(R.id.get_ready_countdown) as TextView
      progressContainer = findViewById(R.id.progress_container) as FrameLayout
      progressView = findViewById(R.id.progress_view) as RingProgressBar
      exerciseCountDown = findViewById(R.id.exercise_countdown) as TextView
    }
  }
}