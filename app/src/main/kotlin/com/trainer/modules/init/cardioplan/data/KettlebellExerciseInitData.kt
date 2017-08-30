package com.trainer.modules.init.cardioplan.data

import com.trainer.modules.training.workout.Exercise
import com.trainer.modules.training.workout.Serie
import com.trainer.modules.training.workout.Workout
import com.trainer.modules.training.workout.types.cyclic.CyclicRoutine

/**
 * Created by dariusz on 20/03/17.
 */
class KettlebellExerciseInitData private constructor() {

  companion object {

    private val SWING = Exercise("Swing")
    private val SQUAT_PRESS_LEFT = Exercise("Squat & Press (Left Arm)")
    private val SWING_BETWEEN_LEGS = Exercise("Swing Between Legs")
    private val SQUAT_PRESS_RIGTH = Exercise("Squat & Press (Right Arm)")
    private val HALO = Exercise("Halo")
    private val LUNGE_PASS = Exercise("Lunge Pass")
    private val HIGH_PULL = Exercise("High Pull")
    private val GOBLET_SQUAT_WITH_ABDUCT_LEFT = Exercise("Goblet Squat (Abduct Left)")
    private val SNATCH = Exercise("Snatch")
    private val GOBLET_SQUAT_WITH_ABDUCT_RIGHT = Exercise("Goblet Squat (Abduct Right)")
    private val SIDE_CHOP_LEFT = Exercise("Side Chop (Left)")
    private val RUSSIAN_TWIST = Exercise("Russian Twist")
    private val SIDE_CHOP_RIGHT = Exercise("Side Chop (Right)")


    val KETTLEBELL_WORKOUT = Workout(arrayListOf(
        Serie.createCycle("Kettlebell Cycle", arrayListOf(
            CyclicRoutine(SWING, 20, 20),
            CyclicRoutine(SQUAT_PRESS_LEFT, 20, 20),
            CyclicRoutine(SWING_BETWEEN_LEGS, 20, 20),
            CyclicRoutine(SQUAT_PRESS_RIGTH, 20, 20),
            CyclicRoutine(HALO, 20, 20),
            CyclicRoutine(LUNGE_PASS, 20, 20),
            CyclicRoutine(HIGH_PULL, 20, 20),
            CyclicRoutine(GOBLET_SQUAT_WITH_ABDUCT_LEFT, 20, 20),
            CyclicRoutine(SNATCH, 20, 20),
            CyclicRoutine(GOBLET_SQUAT_WITH_ABDUCT_RIGHT, 20, 20),
            CyclicRoutine(SIDE_CHOP_LEFT, 20, 20),
            CyclicRoutine(RUSSIAN_TWIST, 20, 20),
            CyclicRoutine(SIDE_CHOP_RIGHT, 20, 0)),
            60)))
  }
}