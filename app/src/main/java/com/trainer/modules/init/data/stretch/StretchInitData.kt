package com.trainer.modules.init.data.stretch

import com.trainer.modules.init.data.stretch.StretchExercises.Companion.SAMPLE_STRETCH_EXERCISE
import com.trainer.modules.training.StretchRoutine
import com.trainer.modules.training.TrainingCategory.*

/**
 * Created by dariusz on 24/01/17.
 */
class StretchInitData {

  companion object {
    val CHEST_STRETCH_ROUTINE = StretchRoutine(CHEST, arrayListOf(
        SAMPLE_STRETCH_EXERCISE, SAMPLE_STRETCH_EXERCISE
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
        // TODO
    ))
  }
}