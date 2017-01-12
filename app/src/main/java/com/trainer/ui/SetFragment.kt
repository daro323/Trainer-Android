package com.trainer.ui

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.arg
import com.trainer.extensions.reduceWithDefault
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.modules.training.WorkoutPresenter.Companion.WEIGHT_NA_VALUE
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
//  private val onInputViewClickListener = { view: View -> (view as EditText).setText("") }

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
  private val weightTypeView: TextView by bindView(R.id.weight_type_text)
  private val inputSeparatorLabelView: TextView by bindView(R.id.input_separator_label)
  private val repInputLabelView: TextView by bindView(R.id.rep_label)

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

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    try {
      if (isVisibleToUser) refreshUi(presenter.getSet(setId))
    } catch (e: UninitializedPropertyAccessException) {
      // Ignore - this callback came too early (earlier than component was injected)
    }
  }

  private fun createUI(set: Set) {
    set.apply {
      // create static content
      val weightType = exercise.weightType
      imageView.setImageResource(set.exercise.imageRes)
      nameView.text = exercise.name
      guidelinesView.text = guidelines.reduceWithDefault("", { item -> "- $item" }, { acc, guideline -> "$acc\n- $guideline" })
      commentsView.text = exercise.comments.reduceWithDefault("", { item -> "- $item" }, { acc, guideline -> "$acc\n- $guideline" })
      lastProgressView.text = lastProgress.map { "$it" }.reduce { acc, repetition -> "$acc\n$repetition" }
      weightInputView.isEnabled = weightType != BODY_WEIGHT
      weightTypeView.text = if (weightType == BODY_WEIGHT) "N/A" else weightType.toString()
      // TODO: Fix mee
//      weightInputView.setOnClickListener(onInputViewClickListener)
//      repInputView.setOnClickListener(onInputViewClickListener)
      // create dynamic content
      refreshUi(this)
    }
  }

  private fun refreshUi(forSet: Set) {
    forSet.apply {
      val iterationIdx = if (forSet.isComplete()) seriesCount - 1 else progress.size   // Counted from zero!
      val iterationNumber = iterationIdx + 1
      require(iterationNumber <= seriesCount) { "Invalid state! current iteration nr= $iterationNumber exceeded the total series count= $seriesCount" }

      setNumberView.text = String.format(getString(R.string.set_number_text), iterationNumber, seriesCount)
      progressView.text = progress.map { "$it" }.reduceWithDefault("", { item -> item }, { acc, repetition -> "$acc\n$repetition" })
      weightInputView.setText(lastProgress[iterationIdx].weight.toString())
      repInputView.setText(lastProgress[iterationIdx].repCount.toString())
      setInputActive(presenter.isCurrentSet(forSet))
    }
  }

  private fun weightValue() = if (weightInputView.isEnabled) weightInputView.text.toString().toFloat() else WEIGHT_NA_VALUE
  private fun repValue() = repInputView.text.toString().toInt()

  private val onSubmitHandler = { v: View ->
    if (FormValidator.validate(activity, fieldsToValidate, SimpleErrorPopupCallback(activity))) {
      presenter.saveSetResult(weightValue(), repValue())
    }
  }

  private fun setInputActive(isActive: Boolean) {
    (if (isActive) VISIBLE else GONE).apply {
      weightInputView.visibility = this
      weightTypeView.visibility = this
      repInputView.visibility = this
      repInputLabelView.visibility = this
      inputSeparatorLabelView.visibility = this
      submitButton.visibility = this
    }
  }
}