package com.trainer.ui

import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.arg
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.utils.bindView
import javax.inject.Inject

/**
 * Created by dariusz on 24/01/17.
 */
class StretchFragment : BaseFragment(R.layout.fragment_stretch) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private var stretchExerciseId: String by arg(ARG_STRETCH_ID)
  private val dummyLabel: TextView by bindView(R.id.dummy_label)

  companion object {
    const val ARG_STRETCH_ID = "ARG_STRETCH_ID"
  }

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    showStretch()
  }

  private fun showStretch() {
    presenter.getCurrentStretchRoutine()?.getStretchExercise(stretchExerciseId)?.apply {
      dummyLabel.text = this.name
      // TODO
    } ?: throw IllegalArgumentException("Attempt to show a stretch but no StretchExercise was found for id= $stretchExerciseId")
  }
}