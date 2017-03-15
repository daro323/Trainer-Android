package com.trainer.ui.training.model

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.trainer.extensions.with
import com.trainer.modules.training.standard.Set
import com.trainer.ui.training.standard.SetFragment
import com.trainer.ui.training.standard.SetFragment.Companion.ARG_SET_ID

/**
 * Created by dariusz on 09/01/17.
 */
class SuperSetPagerAdapter constructor(fragmentManager: FragmentManager,
                                       val data: List<Set>) : FragmentStatePagerAdapter(fragmentManager) {

  override fun getItem(position: Int) = SetFragment().with(ARG_SET_ID to data[position].id())

  override fun getCount() = data.size
}