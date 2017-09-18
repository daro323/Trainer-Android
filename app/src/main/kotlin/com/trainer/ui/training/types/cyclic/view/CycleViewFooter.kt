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
import com.trainer.ui.training.types.cyclic.FooterViewModel
import kotlinx.android.synthetic.main.cycle_view_footer.view.*

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewFooter: FrameLayout {

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
    this.callback = callback.apply { getViewModelChanges().subscribe { updateUI(it.state, it.footerViewModel) } }
  }

  private fun updateUI(state: CycleState, footerViewModel: FooterViewModel) {
    when (state) {
      NEW, GET_READY, RESTING -> {
        next_exercise_label.apply {
          visibleView()
          text = String.format(context.getString(R.string.next_exercise_label), footerViewModel.nextExerciseName)
        }
        finish_view.goneView()
        cycle_progress_container.goneView()
      }

      PERFORMING -> {
        finish_view.goneView()
        next_exercise_label.goneView()
        cycle_progress_container.visibleView()
        cycle_progress_count.text = footerViewModel.currentCount.toString()
        total_cycle_progress_count.text = footerViewModel.totalCount.toString()
        cycle_progress_bar.max = footerViewModel.totalCount
        cycle_progress_bar.progress = footerViewModel.currentCount
      }

      DONE -> {
        finish_view.visibleView()
        next_exercise_label.goneView()
        cycle_progress_container.goneView()
      }

      COMPLETE -> {
        finish_view.goneView()
        next_exercise_label.goneView()
        cycle_progress_container.goneView()
      }
    }
  }

  private fun inflateLayout() {
    inflate(R.layout.cycle_view_footer, this, true).apply {
      finish_view.apply { setOnClickListener { callback.onViewEvent(CycleViewEvent.FINISH) } }
    }
  }
}