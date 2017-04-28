package com.trainer.modules.init.menshilfplan.data.stretch

import com.trainer.modules.init.menshilfplan.InitCategories.*
import com.trainer.modules.init.menshilfplan.data.stretch.ArmsStretchExercises.Companion.ARMS_STRETCH_1
import com.trainer.modules.init.menshilfplan.data.stretch.ArmsStretchExercises.Companion.ARMS_STRETCH_2
import com.trainer.modules.init.menshilfplan.data.stretch.ArmsStretchExercises.Companion.ARMS_STRETCH_3
import com.trainer.modules.init.menshilfplan.data.stretch.BackStretchExercises.Companion.BACK_STRETCH_1
import com.trainer.modules.init.menshilfplan.data.stretch.BackStretchExercises.Companion.BACK_STRETCH_2
import com.trainer.modules.init.menshilfplan.data.stretch.ChestStretchExercises.Companion.CHEST_STRETCH_1
import com.trainer.modules.init.menshilfplan.data.stretch.ChestStretchExercises.Companion.CHEST_STRETCH_2
import com.trainer.modules.init.menshilfplan.data.stretch.ChestStretchExercises.Companion.CHEST_STRETCH_3
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_1
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_10
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_2
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_3
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_4
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_5
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_6
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_7
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_8
import com.trainer.modules.init.menshilfplan.data.stretch.LegsStretchExercises.Companion.LEG_STRETCH_9
import com.trainer.modules.init.menshilfplan.data.stretch.ShouldersStretchExercises.Companion.SHOULDERS_STRETCH_1
import com.trainer.modules.init.menshilfplan.data.stretch.ShouldersStretchExercises.Companion.SHOULDERS_STRETCH_2
import com.trainer.modules.init.menshilfplan.data.stretch.ShouldersStretchExercises.Companion.SHOULDERS_STRETCH_3
import com.trainer.modules.training.types.standard.StretchRoutine

/**
 * Created by dariusz on 24/01/17.
 */
class StretchInitData {

  companion object {
    val CHEST_DAY_STRETCH_ROUTINE = StretchRoutine(CHEST.toString(), arrayListOf(
        CHEST_STRETCH_1, CHEST_STRETCH_2, CHEST_STRETCH_3,
        ARMS_STRETCH_2, BACK_STRETCH_1, ARMS_STRETCH_3
    ))

    val BACK_DAY_STRETCH_ROUTINE = StretchRoutine(BACK.toString(), arrayListOf(
        BACK_STRETCH_1, BACK_STRETCH_2, SHOULDERS_STRETCH_2, SHOULDERS_STRETCH_3
    ))

    val SHOULDERS_DAY_STRETCH_ROUTINE = StretchRoutine(SHOULDERS.toString(), arrayListOf(
        SHOULDERS_STRETCH_3, SHOULDERS_STRETCH_2, SHOULDERS_STRETCH_1,
        BACK_STRETCH_2
    ))

    val ARMS_DAY_STRETCH_ROUTINE = StretchRoutine(ARMS.toString(), arrayListOf(
        ARMS_STRETCH_1, ARMS_STRETCH_2, ARMS_STRETCH_3,
        SHOULDERS_STRETCH_1, BACK_STRETCH_1, SHOULDERS_STRETCH_2,
        BACK_STRETCH_2
    ))

    val LEGS_DAY_STRETCH_ROUTINE = StretchRoutine(LEGS.toString(), arrayListOf(
        LEG_STRETCH_1, LEG_STRETCH_2, LEG_STRETCH_3,
        LEG_STRETCH_4, LEG_STRETCH_5, LEG_STRETCH_6,
        LEG_STRETCH_7, LEG_STRETCH_8, LEG_STRETCH_9, LEG_STRETCH_10
    ))
  }
}