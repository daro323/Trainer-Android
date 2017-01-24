package com.trainer.modules.init.data.stretch

import com.trainer.modules.training.StretchExercise.Companion.createStretch

/**
 * Created by dariusz on 24/01/17.
 */
class StretchExercises {

  companion object {

    val SAMPLE_STRETCH_EXERCISE = createStretch("Sample stretch",
        arrayListOf(
            "Guideline 1", "Guideline 2"
        ))

    val SAMPLE_NO_COMMENT_STRETCH_EXERCISE = createStretch("Sample no comment stretch",
        arrayListOf(
            "Guideline 1", "Guideline 2"
        ))
  }
}