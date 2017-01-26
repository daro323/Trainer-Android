package com.trainer.modules.init.data.stretch

import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_1
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_10
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_2
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_3
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_4
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_5
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_6
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_7
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_8
import com.trainer.modules.init.data.stretch.StretchExercises.Companion.LEG_STRETCH_9
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
        LEG_STRETCH_1, LEG_STRETCH_2, LEG_STRETCH_3,
        LEG_STRETCH_4, LEG_STRETCH_5, LEG_STRETCH_6,
        LEG_STRETCH_7, LEG_STRETCH_8, LEG_STRETCH_9, LEG_STRETCH_10
    ))
  }
}