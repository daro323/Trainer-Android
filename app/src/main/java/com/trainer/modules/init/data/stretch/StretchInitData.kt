package com.trainer.modules.init.data.stretch

import com.trainer.modules.init.data.stretch.ArmsStretchExercises.Companion.ARMS_STRETCH_1
import com.trainer.modules.init.data.stretch.ArmsStretchExercises.Companion.ARMS_STRETCH_2
import com.trainer.modules.init.data.stretch.ArmsStretchExercises.Companion.ARMS_STRETCH_3
import com.trainer.modules.init.data.stretch.BackStretchExercises.Companion.BACK_STRETCH_1
import com.trainer.modules.init.data.stretch.BackStretchExercises.Companion.BACK_STRETCH_2
import com.trainer.modules.init.data.stretch.ChestStretchExercises.Companion.CHEST_STRETCH_1
import com.trainer.modules.init.data.stretch.ChestStretchExercises.Companion.CHEST_STRETCH_2
import com.trainer.modules.init.data.stretch.ChestStretchExercises.Companion.CHEST_STRETCH_3
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_1
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_10
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_2
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_3
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_4
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_5
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_6
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_7
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_8
import com.trainer.modules.init.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_9
import com.trainer.modules.init.data.stretch.ShouldersStretchExercises.Companion.SHOULDERS_STRETCH_1
import com.trainer.modules.init.data.stretch.ShouldersStretchExercises.Companion.SHOULDERS_STRETCH_2
import com.trainer.modules.init.data.stretch.ShouldersStretchExercises.Companion.SHOULDERS_STRETCH_3
import com.trainer.modules.training.StretchRoutine
import com.trainer.modules.training.TrainingCategory.*

/**
 * Created by dariusz on 24/01/17.
 */
class StretchInitData {

  companion object {
    val CHEST_DAY_STRETCH_ROUTINE = StretchRoutine(CHEST, arrayListOf(
        CHEST_STRETCH_1, CHEST_STRETCH_2, CHEST_STRETCH_3,
        ARMS_STRETCH_2, BACK_STRETCH_1, ARMS_STRETCH_3
    ))

    val BACK_DAY_STRETCH_ROUTINE = StretchRoutine(BACK, arrayListOf(
        BACK_STRETCH_1, BACK_STRETCH_2, SHOULDERS_STRETCH_2, SHOULDERS_STRETCH_3
    ))

    val SHOULDERS_DAY_STRETCH_ROUTINE = StretchRoutine(SHOULDERS, arrayListOf(
        SHOULDERS_STRETCH_3, SHOULDERS_STRETCH_2, SHOULDERS_STRETCH_1,
        BACK_STRETCH_2
    ))

    val ARMS_DAY_STRETCH_ROUTINE = StretchRoutine(ARMS, arrayListOf(
        ARMS_STRETCH_1, ARMS_STRETCH_2, ARMS_STRETCH_3,
        SHOULDERS_STRETCH_1, BACK_STRETCH_1, SHOULDERS_STRETCH_2,
        BACK_STRETCH_2
    ))

    val LEGS_DAY_STRETCH_ROUTINE = StretchRoutine(LEGS, arrayListOf(
        LEG_STRETCH_1, LEG_STRETCH_2, LEG_STRETCH_3,
        LEG_STRETCH_4, LEG_STRETCH_5, LEG_STRETCH_6,
        LEG_STRETCH_7, LEG_STRETCH_8, LEG_STRETCH_9, LEG_STRETCH_10
    ))
  }
}