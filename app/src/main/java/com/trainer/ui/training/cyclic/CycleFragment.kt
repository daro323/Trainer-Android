package com.trainer.ui.training.cyclic

import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent

/**
 * Created by dariusz on 15/03/17.
 */
// TODO: Implement meee
class CycleFragment : BaseFragment(R.layout.fragment_pager) {
  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }
}