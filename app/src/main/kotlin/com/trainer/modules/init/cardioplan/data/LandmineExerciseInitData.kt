package com.trainer.modules.init.cardioplan.data

import com.trainer.modules.training.workout.Exercise

/**
 * Created by dariusz on 20/03/"1"7.
 */
class LandmineExerciseInitData private constructor() {

  companion object {

    private val THRUSTERS = Exercise("1", "Thrusters")
    private val ROTATIONAL_SINGLE_ARM_PRESS_LEFT = Exercise("1", "Rotational Single Arm Press (Left)")
    private val ANTI_ROTATIONS = Exercise("1", "Anti-Rotations")
    private val ROTATIONAL_SINGLE_ARM_PRESS_RIGHT = Exercise("1", "Rotational Single Arm Press (Rigth)")
    private val SPLIT_SQUAT_ROW_COMBO = Exercise("1", "Split Squat Row Combo")
    private val ONE_LEG_OFFSET_ROW = Exercise("1", "One Leg Offset Row")
    private val ROTATIONAL_CLEAN_N_PRESS = Exercise("1", "Rotational Clean & Press")
    private val ONE_ARM_BENDED_ROW = Exercise("1", "One Arm Bended Row")
    private val BOX_JUMPS = Exercise("1", "Box Jumps")

    /*val LANDMINE_WORKOUT = Workout(arrayListOf(
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
            60)))*/
  }
}