package com.trainer.ui.training.cyclic

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.extensions.inflate

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewHeader : RelativeLayout {

  private lateinit var header: TextView

  constructor(context: Context) : super(context) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    inflateLayout()
  }

  fun start() {
    header.text = "Duppa"
  }

  private fun inflateLayout() {
    val layout = inflate(R.layout.cycle_view_header, this, true)
    header = layout.findViewById(R.id.cycleHeader) as TextView
  }
}