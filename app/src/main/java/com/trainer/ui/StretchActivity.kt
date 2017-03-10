package com.trainer.ui

import android.app.Activity
import android.os.Bundle
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.startWith
import com.trainer.modules.training.TrainingCategory
import com.trainer.modules.training.TrainingManager
import com.trainer.ui.model.StretchPagerAdapter
import kotlinx.android.synthetic.main.activity_stretch_pager.*
import javax.inject.Inject

/**
 * Created by dariusz on 24/01/17.
 */
class StretchActivity : BaseActivity(R.layout.activity_stretch_pager) {

  @Inject lateinit var trainingManager: TrainingManager
  private lateinit var adapter: StretchPagerAdapter
  private val category by lazy { intent.getIntExtra(ARG_TRAINING_CATEGORY_ORDINAL, -1)
      .apply { if (this == -1) throw IllegalArgumentException("StretchActivity shown without ARG_TRAINING_CATEGORY_ORDINAL!") }
      .run { TrainingCategory.values()[this] }
  }
  private val stretchRoutine by lazy { trainingManager.getStretchPlan().getStretchRoutine(category) }

  companion object {
    const val ARG_TRAINING_CATEGORY_ORDINAL = "ARG_TRAINING_CATEGORY_ORDINAL"

    fun start(trainingCategory: TrainingCategory, context: Activity) {
      context.startWith<StretchActivity> { putExtra(ARG_TRAINING_CATEGORY_ORDINAL, trainingCategory.ordinal) }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    showStretchRoutine()
  }

  private fun showStretchRoutine() {
    if (pager.adapter == null) {
      stretchRoutine?.run {
        title = String.format(getString(R.string.stretch_title), category)
        adapter = StretchPagerAdapter(supportFragmentManager, this.stretchExercises, category.ordinal)
        pager.adapter = adapter
      } ?: throw IllegalStateException("Attempt to show stretch routine when there are nothing set!")
    }
  }
}