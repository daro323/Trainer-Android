package com.trainer.ui.training.stretch

import android.app.Activity
import android.os.Bundle
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.startWith
import com.trainer.modules.training.workout.WorkoutManager
import com.trainer.ui.training.types.model.StretchPagerAdapter
import kotlinx.android.synthetic.main.activity_stretch_pager.*
import javax.inject.Inject

/**
 * Created by dariusz on 24/01/17.
 */
class StretchActivity : BaseActivity(R.layout.activity_stretch_pager) {

  @Inject lateinit var workoutManager: WorkoutManager
  private lateinit var adapter: StretchPagerAdapter
  private val category by lazy { intent.getStringExtra(ARG_TRAINING_CATEGORY)
      .apply { if (this == null) throw IllegalArgumentException("StretchActivity shown without ARG_TRAINING_CATEGORY!") }
  }
  private val stretchRoutine by lazy { workoutManager.getStretchPlan().getStretchRoutine(category) }

  companion object {
    const private val ARG_TRAINING_CATEGORY = "ARG_TRAINING_CATEGORY"

    fun start(trainingCategory: String, context: Activity) {
      context.startWith<StretchActivity> { putExtra(ARG_TRAINING_CATEGORY, trainingCategory) }
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
        adapter = StretchPagerAdapter(supportFragmentManager, this.stretchExercises, category)
        pager.adapter = adapter
      } ?: throw IllegalStateException("Attempt to show stretch routine when there are nothing set!")
    }
  }
}