package com.trainer.ui.model

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.trainer.extensions.with
import com.trainer.modules.training.StretchExercise
import com.trainer.ui.StretchFragment
import com.trainer.ui.StretchFragment.Companion.ARG_STRETCH_ID

/**
 * Created by dariusz on 09/01/17.
 */
class StretchPagerAdapter constructor(fragmentManager: FragmentManager,
                                       val data: List<StretchExercise>) : FragmentStatePagerAdapter(fragmentManager) {

  override fun getItem(position: Int) = StretchFragment().with(ARG_STRETCH_ID to data[position].id)

  override fun getCount() = data.size
}