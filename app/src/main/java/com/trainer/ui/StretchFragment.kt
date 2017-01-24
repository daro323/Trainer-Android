package com.trainer.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.arg
import com.trainer.extensions.reduceWithDefault
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

  private val nameView: TextView by bindView(R.id.name_text)
  private val imageView: ImageView by bindView(R.id.stretch_exercise_image)
  private val guidelinesView: TextView by bindView(R.id.guidelines_text)
  private val commentsView: TextView by bindView(R.id.comments_text)
  private val commentsLabelView: TextView by bindView(R.id.comments_label)

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
      nameView.text = name
      imageView.setImageResource(imageRes)
      guidelinesView.text = guidelines.reduceWithDefault("", { item -> "- $item" }, { acc, guideline -> "$acc\n- $guideline" })

      if (this.comments.isEmpty()) {
        commentsLabelView.visibility = View.GONE
        commentsView.visibility = View.GONE
      } else {
        commentsLabelView.visibility = View.VISIBLE
        commentsView.visibility = View.VISIBLE
        commentsView.text = comments.reduceWithDefault("", { item -> "- $item" }, { acc, guideline -> "$acc\n- $guideline" })
      }

    } ?: throw IllegalArgumentException("Attempt to show a stretch but no StretchExercise was found for id= $stretchExerciseId")
  }
}