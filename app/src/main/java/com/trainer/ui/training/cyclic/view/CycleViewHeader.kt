package com.trainer.ui.training.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.extensions.inflate
import com.trainer.modules.training.types.cyclic.CycleState
import com.trainer.modules.training.types.cyclic.CycleState.*
import com.trainer.ui.training.cyclic.CycleViewCallback
import com.trainer.ui.training.cyclic.HeaderViewModel

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewHeader : FrameLayout {

  private lateinit var header: TextView
  private lateinit var cyclesCount: TextView
  private lateinit var lastCyclesCount: TextView

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
        header.apply {
          visibility = VISIBLE
          text = context.getString(R.string.cycle_new)
        }
        cyclesCount.visibility = GONE
        lastCyclesCount.visibility = GONE
      }

      GET_READY -> {
        header.apply {
          visibility = VISIBLE
          text = context.getString(R.string.cycle_get_ready)
        }
        cyclesCount.visibility = GONE
        lastCyclesCount.visibility = GONE
      }

      PERFORMING -> {
        header.apply {
          visibility = VISIBLE
          text = headerViewModel.exerciseName
        }
        cyclesCount.apply {
          visibility = VISIBLE
          text = String.format(context.getString(R.string.current_cycle), headerViewModel.cycleCount.toString())
        }
        lastCyclesCount.apply {
          visibility = VISIBLE
          text = String.format(context.getString(R.string.last_cycles), headerViewModel.lastCycleCount.toString())
        }
      }

      RESTING -> {
        header.apply {
          visibility = VISIBLE
          text = context.getString(R.string.cycle_resting)
        }
        cyclesCount.visibility = GONE
        lastCyclesCount.visibility = GONE
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
    inflate(R.layout.cycle_view_header, this, true).apply {
      header = findViewById(R.id.cycle_header) as TextView
      cyclesCount = findViewById(R.id.cycle_count) as TextView
      lastCyclesCount = findViewById(R.id.last_cycle_count) as TextView
    }
  }
}