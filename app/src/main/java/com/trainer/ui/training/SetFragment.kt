package com.trainer.ui.training

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.arg
import com.trainer.extensions.reduceWithDefault
import com.trainer.modules.training.ProgressStatus.COMPLETE
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.modules.training.WorkoutPresenter.Companion.WEIGHT_NA_VALUE
import eu.inmite.android.lib.validations.form.FormValidator
import eu.inmite.android.lib.validations.form.annotations.NotEmpty
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback
import kotlinx.android.synthetic.main.fragment_set.*
import javax.inject.Inject

/**
 * Created by dariusz on 09/01/17.
 */
class SetFragment : BaseFragment(R.layout.fragment_set) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // can call this only after component.inject()!
  private var setId: String by arg(ARG_SET_ID)
  private lateinit var set: Set
  private val fieldsToValidate: SetFragmentFieldValidator by lazy { SetFragmentFieldValidator(weightInput, repInput) }
  private val onInputFocusListener = { view: View, hasFocus: Boolean ->
    if (hasFocus) {
      (view as EditText).setText("")
      scrollView.postDelayed({
        if (activity != null && scrollView != null) scrollView.scrollBy(0, scrollView.getChildAt(0).height) }, 200)
    }
  }

  companion object {
    const val ARG_SET_ID = "ARG_SET_ID"
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

  private fun createUI(forSet: Set) {
    set = forSet.apply {
      // create static content
      val weightType = exercise.weightType
      exerciseImage.setImageResource(this.exercise.imageResource())
      nameTextView.text = exercise.name
      guidelinesTextView.text = guidelines.reduceWithDefault("", { item -> "- $item" }, { acc, guideline -> "$acc\n- $guideline" })

      if (exercise.comments.isEmpty()) {
        commentsLabelView.visibility = GONE
        commentsTextView.visibility = GONE
      } else {
        commentsLabelView.visibility = VISIBLE
        commentsTextView.visibility = VISIBLE
        commentsTextView.text = exercise.comments.reduceWithDefault("", { item -> "- $item" }, { acc, guideline -> "$acc\n- $guideline" })
      }

      lastProgressTextView.text = lastProgress.map { "$it" }.reduce { acc, repetition -> "$acc\n$repetition" }
      weightInput.isEnabled = weightType != BODY_WEIGHT
      weightTypeTextView.text = if (weightType == BODY_WEIGHT) "N/A" else weightType.toString()
      weightInput.setOnFocusChangeListener(onInputFocusListener)
      repInput.setOnFocusChangeListener(onInputFocusListener)
      repInput.setOnEditorActionListener { textView, actionId, keyEvent ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          onSubmitHandler(submitButton)
          (textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run { hideSoftInputFromWindow(textView.windowToken, 0) }
          true
        } else {
          false
        }
      }
      // create dynamic content
      refreshUi(this)
    }
  }

  private fun refreshUi(forSet: Set) {
    forSet.apply {
      val iterationIdx = if (forSet.status() == COMPLETE) seriesCount - 1 else progress.size   // Counted from zero!
      val iterationNumber = iterationIdx + 1
      require(iterationNumber <= seriesCount) { "Invalid state! current iteration nr= $iterationNumber exceeded the total series count= $seriesCount" }

      setNumberTextView.text = String.format(getString(R.string.set_number_text), iterationNumber, seriesCount)
      progressTextView.text = progress.map { "$it" }.reduceWithDefault("", { item -> item }, { acc, repetition -> "$acc\n$repetition" })
      weightInput.setText(lastProgress[iterationIdx].weight.toString())
      repInput.setText(lastProgress[iterationIdx].repCount.toString())
      setInputActive(presenter.isCurrentSet(forSet))
    }
  }

  private fun weightValue() = if (weightInput.isEnabled) weightInput.text.toString().toFloat() else WEIGHT_NA_VALUE
  private fun repValue() = repInput.text.toString().toInt()

  private val onSubmitHandler = { v: View ->
    if (FormValidator.validate(activity, fieldsToValidate, SimpleErrorPopupCallback(activity))) {
      presenter.saveSetResult(weightValue(), repValue())
      refreshUi(set)
    }
  }

  private fun setInputActive(isActive: Boolean) {
    (if (isActive) VISIBLE else GONE).apply {
      weightInput.visibility = this
      weightTypeTextView.visibility = this
      repInput.visibility = this
      repLabel.visibility = this
      inputSeparatorLabelView.visibility = this
      submitButton.visibility = this
    }
  }
}

class SetFragmentFieldValidator constructor(
    @NotEmpty(messageId = R.string.empty_field)
    val weightInput: EditText,
    @NotEmpty(messageId = R.string.empty_field)
    val repInput: EditText
)