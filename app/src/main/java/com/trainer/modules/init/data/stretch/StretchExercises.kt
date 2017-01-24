package com.trainer.modules.init.data.stretch

import com.trainer.R
import com.trainer.modules.training.StretchExercise.Companion.createStretch

/**
 * Created by dariusz on 24/01/17.
 */
class StretchExercises {

  companion object {

    val SAMPLE_STRETCH_EXERCISE = createStretch("Sample stretch", R.mipmap.ic_exercise_default,
        arrayListOf(
            "Guideline 1", "Guideline 2"
        ),
        arrayListOf(
            "Comment 1", "Comment 2"
        ))
  }
}