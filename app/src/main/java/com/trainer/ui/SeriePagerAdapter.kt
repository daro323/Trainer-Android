package com.trainer.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.trainer.extensions.with
import com.trainer.modules.training.Series
import com.trainer.ui.SetFragment.Companion.SET_ID

/**
 * Created by dariusz on 09/01/17.
 */
class SeriePagerAdapter constructor(fragmentManager: FragmentManager,
                                    val data: List<Series>) : FragmentStatePagerAdapter(fragmentManager) {

  override fun getItem(position: Int) = SetFragment().with(SET_ID to data[position].id())

  override fun getCount() = data.size
}