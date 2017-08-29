package com.trainer.modules.init.cardioplan.data

import com.trainer.modules.training.Exercise
import com.trainer.modules.training.Serie
import com.trainer.modules.training.Workout
import com.trainer.modules.training.types.cyclic.CyclicRoutine

/**
 * Created by dariusz on 20/03/17.
 */
class BoxingExerciseInitData private constructor() {

  companion object {

    private val LEFT_JAB = Exercise("Left Jab")
    private val RIGHT_CROSS = Exercise("Right Cross")
    private val LEFT_BODY = Exercise("Left Body")
    private val RIGHT_BODY = Exercise("Right Body")
    private val LEFT_HOOK = Exercise("Left Hook")
    private val RIGHT_HOOK = Exercise("Right Hook")
    private val ALTERNATING_PUNCHES_WITH_SIDE_PUNCH = Exercise("Alternating Punches with Side Punch")

    val BOXING_WORKOUT = Workout(arrayListOf(
        Serie.createCycle("Boxing Cycle", arrayListOf(
            CyclicRoutine(LEFT_JAB, 35, 20),
            CyclicRoutine(RIGHT_CROSS, 35, 20),
            CyclicRoutine(LEFT_BODY, 35, 20),
            CyclicRoutine(RIGHT_BODY, 35, 20),
            CyclicRoutine(LEFT_HOOK, 35, 20),
            CyclicRoutine(RIGHT_HOOK, 35, 20),
            CyclicRoutine(ALTERNATING_PUNCHES_WITH_SIDE_PUNCH, 45, 0)),
            60)))
  }
}