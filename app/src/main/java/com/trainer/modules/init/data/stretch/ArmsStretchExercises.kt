package com.trainer.modules.init.data.stretch

import com.trainer.R
import com.trainer.modules.training.types.standard.StretchExercise.Companion.createStretch

/**
 * Created by dariusz on 24/01/17.
 */
class ArmsStretchExercises private constructor() {

  companion object {

    val ARMS_STRETCH_1 = createStretch(R.drawable.str_arms_1)

    val ARMS_STRETCH_2 = createStretch(R.drawable.str_arms_2)

    val ARMS_STRETCH_3 = createStretch(R.drawable.str_arms_3)
  }
}