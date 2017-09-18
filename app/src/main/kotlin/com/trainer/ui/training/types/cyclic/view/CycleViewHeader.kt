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
import com.trainer.ui.training.types.cyclic.HeaderViewModel
import kotlinx.android.synthetic.main.cycle_view_header.view.*

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewHeader : FrameLayout {

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
    this.callback = callback.apply { getViewModelChanges().subscribe { updateUI(it.state, it.headerViewModel) } }
  }

  private fun updateUI(state: CycleState, headerViewModel: HeaderViewModel) {
    when (state) {
      NEW -> {
        cycle_header.apply {
          visibleView()
          text = context.getString(R.string.cycle_new)
        }
        cycle_count.goneView()
        last_cycle_count.apply {
          visibleView()
          text = String.format(context.getString(R.string.last_cycles), headerViewModel.lastCycleCount.toString())
        }
      }

      GET_READY -> {
        cycle_header.apply {
          visibleView()
          text = context.getString(R.string.cycle_get_ready)
        }
        cycle_count.goneView()
        last_cycle_count.goneView()
      }

      PERFORMING -> {
        cycle_header.apply {
          visibleView()
          text = headerViewModel.exerciseName
        }
        cycle_count.apply {
          visibleView()
          text = String.format(context.getString(R.string.current_cycle), headerViewModel.cycleCount.toString())
        }
        last_cycle_count.apply {
          visibleView()
          text = String.format(context.getString(R.string.last_cycles), headerViewModel.lastCycleCount.toString())
        }
      }

      RESTING -> {
        cycle_header.apply {
          visibleView()
          text = context.getString(R.string.cycle_resting)
        }
        cycle_count.goneView()
        last_cycle_count.goneView()
      }

      DONE -> {
        cycle_header.apply {
          visibleView()
          text = context.getString(R.string.cycle_done)
        }
        cycle_count.apply {
          visibleView()
          text = String.format(context.getString(R.string.current_cycle), headerViewModel.cycleCount.toString())
        }
        last_cycle_count.apply {
          visibleView()
          text = String.format(context.getString(R.string.last_cycles), headerViewModel.lastCycleCount.toString())
        }
      }

      COMPLETE -> {
        cycle_header.apply {
          visibleView()
          text = context.getString(R.string.cycle_complete)
        }
        cycle_count.apply {
          visibleView()
          text = String.format(context.getString(R.string.current_cycle), headerViewModel.cycleCount.toString())
        }
        last_cycle_count.apply {
          visibleView()
          text = String.format(context.getString(R.string.last_cycles), headerViewModel.lastCycleCount.toString())
        }
      }
    }
  }

  private fun inflateLayout() {
    inflate(R.layout.cycle_view_header, this, true)
  }
}