package com.trainer.ui.training.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.trainer.R
import com.trainer.extensions.inflate

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewBody: FrameLayout {

  constructor(context: Context) : super(context) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    inflateLayout()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    inflateLayout()
  }

  private fun inflateLayout() {
    val layout = inflate(R.layout.cycle_view_body, this, true)
  }
}