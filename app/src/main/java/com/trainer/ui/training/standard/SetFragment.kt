package com.trainer.ui.training.standard

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.model.CoreConstants.Companion.WEIGHT_VALUE_NOT_APPLICABLE
import com.trainer.core.training.model.ProgressStatus.COMPLETE
import com.trainer.core.training.model.Repetition
import com.trainer.core.training.model.WeightType.BODY_WEIGHT
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.arg
import com.trainer.extensions.reduceWithDefault
import com.trainer.modules.training.standard.Set
import com.trainer.modules.training.standard.StandardPresenterHelper
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
  private val presenterHelper: StandardPresenterHelper by lazy { (trainingManager.workoutPresenter?.getHelper() ?: throw IllegalStateException("Current workout presenter not set!") ) as StandardPresenterHelper }  // can call this only after component.inject()!
  private var setId: String by arg(ARG_SET_ID)
  private lateinit var set: Set
  private val fieldsToValidate: SetFragmentFieldValidator by lazy { SetFragmentFieldValidator(weightInput, repInput) }
  private val onInputFocusListener = { view: View, hasFocus: Boolean ->
    if (hasFocus) {
      (view as EditText).apply {
        if (activity != null) this.postDelayed({ setText("") }, 100)
      }

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
    createUI(presenterHelper.getSet(setId))
    submitButton.setOnClickListener(onSubmitHandler)
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    try {
      if (isVisibleToUser) refreshUi(presenterHelper.getSet(setId))
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
      weightTypeTextView.text = if (weightType != BODY_WEIGHT) weightType.toString() else ""
      weightInput.setOnFocusChangeListener(onInputFocusListener)
      repInput.setOnFocusChangeListener(onInputFocusListener)
      repInput.setOnEditorActionListener { textView, actionId, keyEvent ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          onSubmitHandler(submitButton)
          true
        } else {
          false
        }
      }
      // create dynamic content
      refreshUi(this)
    }
  }

  private fun refreshUi(forSet: Set, onSubmit: Boolean = false) {
    forSet.apply {
      val iterationIdx = if (forSet.status() == COMPLETE) seriesCount - 1 else progress.size   // Counted from zero!
      val iterationNumber = if (presenterHelper.isCurrentSet(forSet) && status() != COMPLETE) progress.size + 1 else progress.size  // Counted from one!

      require(iterationNumber <= seriesCount) { "Invalid state! current iteration nr= $iterationNumber exceeded the total series count= $seriesCount" }

      val rep = if (onSubmit) progress.last() else lastProgress[iterationIdx]  // On submit show current result (don't swap result of next rep)

      weightInput.setText(rep.weight.toString())
      repInput.setText(rep.repCount.toString())
      setNumberTextView.text = String.format(getString(R.string.set_number_text), iterationNumber, seriesCount)
      progressTextView.text = progress.map { "$it" }.reduceWithDefault("", { item -> item }, { acc, repetition -> "$acc\n$repetition" })
      setInputActive(presenterHelper.isCurrentSet(forSet), rep)
      (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run { hideSoftInputFromWindow(set_container.windowToken, 0) }
    }
  }

  private fun weightValue() = if (weightInput.visibility == VISIBLE) weightInput.text.toString().toFloat() else WEIGHT_VALUE_NOT_APPLICABLE
  private fun repValue() = repInput.text.toString().toInt()

  private val onSubmitHandler = { v: View ->
    if (FormValidator.validate(activity, fieldsToValidate, SimpleErrorPopupCallback(activity))) {
      if (isRepetitionInValid()) {
        Toast.makeText(activity, R.string.result_missing_rep_count, Toast.LENGTH_SHORT).show()
      } else {
        presenterHelper.saveSetResult(weightValue(), repValue())
        refreshUi(set, true)
      }
    }
  }

  private fun isRepetitionInValid() = (weightValue() > 0F && repValue() == 0)

  private fun setInputActive(isActive: Boolean, forRep: Repetition) {
    val isActiveVisibility = if (isActive) VISIBLE else GONE
    val alsoForBodyType = if (isActive && forRep.weightType != BODY_WEIGHT) VISIBLE else GONE

    repInput.visibility = isActiveVisibility
    repLabel.visibility = isActiveVisibility
    submitButton.visibility = isActiveVisibility

    weightInput.visibility = alsoForBodyType
    weightTypeTextView.visibility = alsoForBodyType
    inputSeparatorLabelView.visibility = alsoForBodyType
  }
}

class SetFragmentFieldValidator constructor(
    @NotEmpty(messageId = R.string.empty_field)
    val weightInput: EditText,
    @NotEmpty(messageId = R.string.empty_field)
    val repInput: EditText
)