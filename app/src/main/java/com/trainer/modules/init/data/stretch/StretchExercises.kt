package com.trainer.modules.init.data.stretch

import com.trainer.R
import com.trainer.modules.training.StretchExercise.Companion.createStretch

/**
 * Created by dariusz on 24/01/17.
 */
class StretchExercises {

  companion object {

    // LEGS ----------------------------------------------------------------------------------------------------
    val QUADS_STRETCH = createStretch("Roźciąganie uda w oparciu",
        arrayListOf("Roźciągaj 30-60 sekund na noge."
        ),
        arrayListOf("Stań tyłem do rolki i oprzyj na niej goleń.",
            "W tej pozycji oprzyj się o swoją piętę."
        ), R.drawable.str_quad)

    val TOUCH_YOUR_TOES_DOWN = createStretch("Dotknij swoich palców u nóg",
        arrayListOf("Roźciągaj 15 sekund."
        ),
        arrayListOf("Na wyprostowanych nogach pochyl się do przodu i spróbuj chwycić swoje palce u nóg.",
            "Dla utrudnienia możesz skrzyżować nogi."
        ), R.drawable.str_touch_toes_down)

    val TOUCH_YOUR_TOES_UP = createStretch("Oprzyj nogę i dotknij palców",
        arrayListOf("Roźciągaj 30-60 sekund na nogę."
        ),
        arrayListOf("Oprzyj nogę na wysokości klatki.",
            "W tej pozycji spróbuj dotknąć palców u nogi."
        ), R.drawable.str_touch_toes_up)
  }
}