package com.trainer.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.ui.model.StretchPagerAdapter
import com.trainer.utils.bindView
import javax.inject.Inject

/**
 * Created by dariusz on 24/01/17.
 */
class StretchActivity : BaseActivity(R.layout.activity_stretch_pager) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private lateinit var adapter: StretchPagerAdapter
  private val pager: ViewPager by bindView(R.id.pager_view)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    showStretchRoutine()
  }

  private fun showStretchRoutine() {
    presenter.getCurrentStretchRoutine()?.run {
      title = String.format(getString(R.string.stretch_title), category)
      adapter = StretchPagerAdapter(supportFragmentManager, this.stretchExercises)
      pager.adapter = adapter
    } ?: throw IllegalStateException("Attempt to show stretch routine when there are nothing set!")
  }
}