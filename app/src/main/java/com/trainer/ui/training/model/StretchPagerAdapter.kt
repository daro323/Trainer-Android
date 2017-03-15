package com.trainer.ui.training.model

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.trainer.extensions.with
import com.trainer.modules.training.standard.StretchExercise
import com.trainer.ui.training.StretchFragment
import com.trainer.ui.training.StretchFragment.Companion.ARG_STRETCH_EXERCISE_IDX
import com.trainer.ui.training.StretchFragment.Companion.ARG_TRAINING_CATEGORY_ORDINAL

/**
 * Created by dariusz on 09/01/17.
 */
class StretchPagerAdapter constructor(fragmentManager: FragmentManager,
                                      val data: List<StretchExercise>,
                                      val trainingCategoryOrdinal: Int) : FragmentStatePagerAdapter(fragmentManager) {

  override fun getItem(position: Int) = StretchFragment().with(
      ARG_STRETCH_EXERCISE_IDX to position,
      ARG_TRAINING_CATEGORY_ORDINAL to trainingCategoryOrdinal)

  override fun getCount() = data.size
}