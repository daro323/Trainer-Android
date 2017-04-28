package com.trainer.modules.init.cardioplan.data

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.Workout
import com.trainer.modules.training.types.cyclic.CyclicRoutine

/**
 * Created by dariusz on 20/03/17.
 */
class LandmineExerciseInitData private constructor() {

  companion object {

    private val THRUSTERS = Exercise("Thrusters")
    private val ROTATIONAL_SINGLE_ARM_PRESS_LEFT = Exercise("Rotational Single Arm Press (Left)")
    private val ANTI_ROTATIONS = Exercise("Anti-Rotations")
    private val ROTATIONAL_SINGLE_ARM_PRESS_RIGHT = Exercise("Rotational Single Arm Press (Rigth)")
    private val SPLIT_SQUAT_ROW_COMBO = Exercise("Split Squat Row Combo")
    private val ONE_LEG_OFFSET_ROW = Exercise("One Leg Offset Row")
    private val ROTATIONAL_CLEAN_N_PRESS = Exercise("Rotational Clean & Press")
    private val ONE_ARM_BENDED_ROW = Exercise("One Arm Bended Row")
    private val BOX_JUMPS = Exercise("Box Jumps")

    val LANDMINE_WORKOUT = Workout(arrayListOf(
        Serie.createCycle("Landmine Cycle", arrayListOf(
            CyclicRoutine(THRUSTERS, 20, 20),
            CyclicRoutine(ROTATIONAL_SINGLE_ARM_PRESS_LEFT, 20, 20),
            CyclicRoutine(ANTI_ROTATIONS, 20, 20),
            CyclicRoutine(ROTATIONAL_SINGLE_ARM_PRESS_RIGHT, 20, 20),
            CyclicRoutine(SPLIT_SQUAT_ROW_COMBO, 20, 20),
            CyclicRoutine(ONE_LEG_OFFSET_ROW, 20, 20),
            CyclicRoutine(ROTATIONAL_CLEAN_N_PRESS, 20, 20),
            CyclicRoutine(ONE_ARM_BENDED_ROW, 20, 20),
            CyclicRoutine(BOX_JUMPS, 20, 0)),
            60)))
  }
}