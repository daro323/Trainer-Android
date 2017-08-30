package com.trainer.ui.training.types.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.*
import com.trainer.R
import com.trainer.extensions.goneView
import com.trainer.extensions.inflate
import com.trainer.extensions.visibleView
import com.trainer.modules.training.workout.types.cyclic.CycleState
import com.trainer.modules.training.workout.types.cyclic.CycleState.*
import com.trainer.ui.training.types.cyclic.CycleViewCallback
import com.trainer.ui.training.types.cyclic.CycleViewEvent
import com.trainer.ui.training.types.cyclic.FooterViewModel

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewFooter: FrameLayout {

  private lateinit var finishView: Button
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
      NEW, GET_READY, RESTING -> {
        nextExercise.apply {
          visibleView()
          text = String.format(context.getString(R.string.next_exercise_label), footerViewModel.nextExerciseName)
        }
        finishView.goneView()
        cycleProgressContainer.goneView()
      }

      PERFORMING -> {
        finishView.goneView()
        nextExercise.goneView()
        cycleProgressContainer.visibleView()
        cycleProgressCount.text = footerViewModel.currentCount.toString()
        totalCycleProgressCount.text = footerViewModel.totalCount.toString()
        cycleProgressBar.max = footerViewModel.totalCount
        cycleProgressBar.progress = footerViewModel.currentCount
      }

      DONE -> {
        finishView.visibleView()
        nextExercise.goneView()
        cycleProgressContainer.goneView()
      }

      COMPLETE -> {
        finishView.goneView()
        nextExercise.goneView()
        cycleProgressContainer.goneView()
      }
    }
  }

  private fun inflateLayout() {
    inflate(R.layout.cycle_view_footer, this, true).apply {
      finishView = (findViewById(R.id.finish_view) as Button).apply { setOnClickListener { callback.onViewEvent(CycleViewEvent.FINISH) } }
      nextExercise = findViewById(R.id.next_exercise_label) as TextView
      cycleProgressContainer = findViewById(R.id.cycle_progress_container) as RelativeLayout
      cycleProgressCount = findViewById(R.id.cycle_progress_count) as TextView
      totalCycleProgressCount = findViewById(R.id.total_cycle_progress_count) as TextView
      cycleProgressBar = findViewById(R.id.cycle_progress_bar) as ProgressBar
    }
  }
}