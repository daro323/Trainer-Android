package com.trainer.ui.training

import android.view.View
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.arg
import com.trainer.extensions.reduceWithDefault
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.coredata.TrainingCategory
import kotlinx.android.synthetic.main.fragment_stretch.*
import javax.inject.Inject

/**
 * Created by dariusz on 24/01/17.
 */
class StretchFragment : BaseFragment(R.layout.fragment_stretch) {

  @Inject lateinit var trainingManager: TrainingManager
  private var stretchExerciseIdx: Int by arg(ARG_STRETCH_EXERCISE_IDX, VALUE_NOT_SET)
  private val categoryOrdinal: Int by arg(ARG_TRAINING_CATEGORY_ORDINAL, VALUE_NOT_SET)


  companion object {
    const val ARG_STRETCH_EXERCISE_IDX = "ARG_STRETCH_EXERCISE_IDX"
    const val ARG_TRAINING_CATEGORY_ORDINAL = "ARG_TRAINING_CATEGORY_ORDINAL"
    const val VALUE_NOT_SET = -1
  }

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    showStretch()
  }

  private fun showStretch() {
    require(categoryOrdinal != VALUE_NOT_SET) { "StretchFragment without ARG_TRAINING_CATEGORY_ORDINAL set!" }
    require(stretchExerciseIdx != VALUE_NOT_SET) { "StretchFragment without ARG_STRETCH_EXERCISE_IDX set!" }

    categoryOrdinal
        .run { TrainingCategory.values()[this] }
        .run { trainingManager.getStretchPlan().getStretchRoutine(this) }
        ?.run { stretchExercises[stretchExerciseIdx] }
        ?.apply {
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
        } ?: throw IllegalStateException("Can't show Stretch Exercise - exercise not found for index= $stretchExerciseIdx for category= ${TrainingCategory.values()[categoryOrdinal]}")
  }
}