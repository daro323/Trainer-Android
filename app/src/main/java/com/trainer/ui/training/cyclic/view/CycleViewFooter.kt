package com.trainer.ui.training.cyclic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.trainer.R
import com.trainer.extensions.inflate
import com.trainer.ui.training.cyclic.CycleState
import com.trainer.ui.training.cyclic.CycleViewModel
import com.trainer.ui.training.cyclic.FooterViewModel
import io.reactivex.Observable

/**
 * Created by dariusz on 18/03/17.
 */
class CycleViewFooter: FrameLayout {

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
    modelObservable
        .subscribe { updateUI(it.state, it.footerViewModel) }
  }

  private fun updateUI(state: CycleState, footerViewModel: FooterViewModel) {

  }

  private fun inflateLayout() {
    val layout = inflate(R.layout.cycle_view_footer, this, true)
  }
}