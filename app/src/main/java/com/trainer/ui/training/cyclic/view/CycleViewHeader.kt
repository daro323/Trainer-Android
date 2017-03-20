package com.trainer.ui.training.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.extensions.inflate
import com.trainer.ui.training.cyclic.CycleState
import com.trainer.ui.training.cyclic.CycleState.*
import com.trainer.ui.training.cyclic.CycleViewModel
import com.trainer.ui.training.cyclic.HeaderViewModel
import io.reactivex.Observable

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewHeader : RelativeLayout {

  private lateinit var header: TextView
  private lateinit var cyclesCount: TextView
  private lateinit var lastCyclesCount: TextView

  constructor(context: Context) : super(context) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    inflateLayout()
  }

  fun bindViewModel(modelObservable: Observable<CycleViewModel>) {
    modelObservable.subscribe { updateUI(it.state, it.headerViewModel) }
  }

  private fun updateUI(state: CycleState, hearedViewModel: HeaderViewModel) {
    when (state) {
      NEW -> {
        header.text = context.getString(R.string.cycle_new)
        cyclesCount.visibility = GONE
        lastCyclesCount.visibility = GONE
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
    inflate(R.layout.cycle_view_header, this, true).apply {
      header = findViewById(R.id.cycle_header) as TextView
      cyclesCount = findViewById(R.id.cycle_count) as TextView
      lastCyclesCount = findViewById(R.id.last_cycle_count) as TextView
    }
  }
}