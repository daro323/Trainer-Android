package com.trainer.ui.model

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.trainer.extensions.with
import com.trainer.modules.training.Series.Set
import com.trainer.ui.SetFragment
import com.trainer.ui.SetFragment.Companion.SET_ID

/**
 * Created by dariusz on 09/01/17.
 */
class SuperSetPagerAdapter constructor(fragmentManager: FragmentManager,
                                       val data: List<Set>) : FragmentStatePagerAdapter(fragmentManager) {

  override fun getItem(position: Int) = SetFragment().with(SET_ID to data[position].id())

  override fun getCount() = data.size
}