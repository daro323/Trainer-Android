package com.trainer.ui.training.types.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.trainer.R
import com.trainer.extensions.goneView
import com.trainer.extensions.inflate
import com.trainer.extensions.visibleView
import com.trainer.modules.training.workout.types.cyclic.CycleState
import com.trainer.modules.training.workout.types.cyclic.CycleState.*
import com.trainer.ui.training.types.cyclic.CycleViewCallback
import com.trainer.ui.training.types.cyclic.CycleViewEvent
import com.trainer.ui.training.types.cyclic.CycleViewModel
import kotlinx.android.synthetic.main.activity_rest.view.*
import kotlinx.android.synthetic.main.cycle_view_body.view.*

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewBody: FrameLayout {

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
    this.callback = callback.apply { getViewModelChanges().subscribe { updateUI(it.state, it) } }
  }

  private fun updateUI(state: CycleState, cycleViewModel: CycleViewModel) {
    val bodyViewModel = cycleViewModel.bodyViewModel

    when (state) {
      NEW -> {
        if (cycleViewModel.isActive) start_button.visibleView() else start_button.goneView()
        do_more_button.goneView()
        get_ready_countdown.goneView()
        progress_container.goneView()
      }

      GET_READY -> {
        start_button.goneView()
        do_more_button.goneView()
        progress_container.goneView()
        get_ready_countdown.apply {
          visibleView()
          text = bodyViewModel.countDown.toString()
        }
      }

      PERFORMING, RESTING -> {
        start_button.goneView()
        do_more_button.goneView()
        get_ready_countdown.goneView()
        progress_container.visibleView()
        progressView.apply {
          max = bodyViewModel.totalCountDown
          progress = bodyViewModel.countDown
        }
        exercise_countdown.text = String.format(context.getString(R.string.countdown_text), bodyViewModel.countDown)
      }

      DONE -> {
        start_button.goneView()
        do_more_button.visibleView()
        get_ready_countdown.goneView()
        progress_container.goneView()
      }

      COMPLETE -> {
        start_button.goneView()
        do_more_button.goneView()
        get_ready_countdown.goneView()
        progress_container.goneView()
      }
    }
  }

  private fun inflateLayout() {
    inflate(R.layout.cycle_view_body, this, true).apply {
      start_button.apply { setOnClickListener { callback.onViewEvent(CycleViewEvent.START) } }
      do_more_button.apply { setOnClickListener { callback.onViewEvent(CycleViewEvent.ONE_MORE) } }
    }
  }
}