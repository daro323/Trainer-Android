package com.trainer.ui.training.standard

import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent
import com.trainer.ui.training.model.SuperSetPagerAdapter

/**
 * Created by dariusz on 15/03/17.
 */
// TODO: Implement mee
class SuperSetFragment : BaseFragment(R.layout.fragment_pager) {
  private lateinit var adapter: SuperSetPagerAdapter

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }
}