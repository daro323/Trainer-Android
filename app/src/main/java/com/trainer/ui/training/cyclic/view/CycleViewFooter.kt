package com.trainer.ui.training.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.extensions.inflate
import com.trainer.modules.training.types.cyclic.CycleState
import com.trainer.modules.training.types.cyclic.CycleState.*
import com.trainer.ui.training.cyclic.CycleViewCallback
import com.trainer.ui.training.cyclic.FooterViewModel

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewFooter: FrameLayout {

  private lateinit var finishView: TextView
  private lateinit var nextExercise: TextView
  private lateinit var cycleProgressContainer: RelativeLayout
  private lateinit var cycleProgressCount: TextView
  private lateinit var totalCycleProgressCount: TextView
  private lateinit var cycleProgressBar: ProgressBar

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
      NEW -> {
        nextExercise.apply {
          visibility = VISIBLE
          text = String.format(context.getString(R.string.next_exercise_label), footerViewModel.nextExerciseName)
        }
        finishView.visibility = GONE
        cycleProgressContainer.visibility = GONE
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
    inflate(R.layout.cycle_view_footer, this, true).apply {
      finishView = findViewById(R.id.finish_view) as TextView
      nextExercise = findViewById(R.id.next_exercise_label) as TextView
      cycleProgressContainer = findViewById(R.id.cycle_progress_container) as RelativeLayout
      cycleProgressCount = findViewById(R.id.cycle_progress_count) as TextView
      totalCycleProgressCount = findViewById(R.id.total_cycle_progress_count) as TextView
      cycleProgressBar = findViewById(R.id.cycle_progress_bar) as ProgressBar
    }
  }
}