package com.trainer.ui

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.arg
import com.trainer.extensions.reduceWithDefault
import com.trainer.modules.training.Repetition
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.workout.WorkoutPresenter
import com.trainer.ui.model.SetFragmentFieldValidator
import com.trainer.utils.bindView
import eu.inmite.android.lib.validations.form.FormValidator
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback
import javax.inject.Inject

/**
 * Created by dariusz on 09/01/17.
 */
class SetFragment : BaseFragment(R.layout.fragment_set) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // can call this only after component.inject()!
  private var setId: String by arg(SET_ID)
  private val fieldsToValidate: SetFragmentFieldValidator by lazy { SetFragmentFieldValidator(weightInputView, repInputView) }

  private val imageView: ImageView by bindView(R.id.exercise_image)
  private val nameView: TextView by bindView(R.id.name_text)
  private val setNumberView: TextView by bindView(R.id.set_text)
  private val guidelinesView: TextView by bindView(R.id.guidelines_text)
  private val commentsView: TextView by bindView(R.id.comments_text)
  private val progressView: TextView by bindView(R.id.progress_text)
  private val lastProgressView: TextView by bindView(R.id.last_progress_text)
  private val weightInputView: EditText by bindView(R.id.weight_input)
  private val repInputView: EditText by bindView(R.id.rep_input)
  private val submitButton: Button by bindView(R.id.submit_button)

  companion object {
    const val SET_ID = "SET_ID"
  }

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    createUI(presenter.getSet(setId))
    submitButton.setOnClickListener(onSubmitHandler)
  }

  private fun createUI(set: Set) {
    set.apply {
      val iteration = if (progress.isEmpty()) 0 else progress.indexOf(progress.last())    // Counted from zero!

      imageView.setImageResource(set.exercise.imageRes)
      nameView.text = exercise.name
      setNumberView.text = String.format(getString(R.string.set_number_text), iteration + 1, seriesCount)
      guidelinesView.text = guidelines.reduceWithDefault("", { item -> "- $item"}, { acc, guideline -> "$acc\n- $guideline" })
      commentsView.text = exercise.comments.reduceWithDefault("", { item -> "- $item"}, { acc, guideline -> "$acc\n- $guideline" })
      progressView.text = progress.map { "$it" }.reduceWithDefault("", { item -> item }, { acc, repetition -> "$acc\n$repetition"  })
      lastProgressView.text = lastProgress.map { "$it" }.reduce { acc, repetition -> "$acc\n$repetition"  }
      weightInputView.setText(lastProgress[iteration].weight.toString())
      repInputView.setText(lastProgress[iteration].repCount.toString())
    }
  }

  private fun weightValue() = Integer.valueOf(weightInputView.text.toString())
  private fun repValue() = Integer.valueOf(repInputView.text.toString())

  private val onSubmitHandler = { v: View ->
    if (FormValidator.validate(activity, fieldsToValidate, SimpleErrorPopupCallback(activity))) {
      presenter.saveSetResult(Repetition(repValue(), weightValue()))
    }
  }
}