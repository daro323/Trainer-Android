package com.trainer.modules.init.data.stretch

import com.trainer.modules.init.data.stretch.StretchExercises.Companion.QUADS_STRETCH
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.TOUCH_YOUR_TOES_DOWN
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.TOUCH_YOUR_TOES_UP
import com.trainer.modules.training.StretchRoutine
import com.trainer.modules.training.TrainingCategory.*

/**
 * Created by dariusz on 24/01/17.
 */
class StretchInitData {

  companion object {
    val CHEST_STRETCH_ROUTINE = StretchRoutine(CHEST, arrayListOf(
        // TODO
    ))

    val BACK_STRETCH_ROUTINE = StretchRoutine(BACK, arrayListOf(
        // TODO
    ))

    val SHOULDERS_STRETCH_ROUTINE = StretchRoutine(SHOULDERS, arrayListOf(
        // TODO
    ))

    val ARMS_STRETCH_ROUTINE = StretchRoutine(ARMS, arrayListOf(
        // TODO
    ))

    val LEGS_STRETCH_ROUTINE = StretchRoutine(LEGS, arrayListOf(
        QUADS_STRETCH,
        TOUCH_YOUR_TOES_DOWN,
        TOUCH_YOUR_TOES_UP
        // TODO
    ))
  }
}