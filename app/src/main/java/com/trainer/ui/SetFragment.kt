package com.trainer.ui

import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent

/**
 * Created by dariusz on 09/01/17.
 */
class SetFragment : BaseFragment() {

  companion object {
    const val SET_ID = "SET_ID"
  }

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }
}