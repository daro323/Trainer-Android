package com.trainer.modules.init.data.stretch

import com.trainer.R
import com.trainer.modules.training.types.standard.StretchExercise.Companion.createStretch

/**
 * Created by dariusz on 24/01/17.
 */
class BackStretchExercises private constructor() {

  companion object {

    val BACK_STRETCH_1 = createStretch(R.drawable.str_back_1,
        arrayListOf("Obniż biodra.",
            "Skręć i wygnij tułów."))

    val BACK_STRETCH_2 = createStretch(R.drawable.str_back_2,
        arrayListOf("Unieś barki podczas rozciągania."))
  }
}