package com.trainer.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.modules.training.Series
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.workout.WorkoutPresenter
import com.trainer.utils.bindView
import javax.inject.Inject

class SeriePagerActivity : BaseActivity(R.layout.fragment_set) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()

  private val pager: ViewPager by bindView(R.id.pager_view)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    showSerie(trainingManager.workoutPresenter?.getCurrentSerie() ?: throw IllegalStateException("Current serie is not selected!"))
  }

  private fun showSerie(serie: Series) {
    // TODO
    // Build either fragment or paged fragments
  }
}
