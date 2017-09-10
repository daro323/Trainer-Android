package com.trainer.modules.init.cardioplan.data

import com.trainer.persistence.training.TrainingPlanDao.Exercise


/**
 * Created by dariusz on 20/03/"1"7.
 */
class BoxingExerciseInitData private constructor() {

  companion object {

    private val LEFT_JAB = Exercise("1", "Left Jab")
    private val RIGHT_CROSS = Exercise("1", "Right Cross")
    private val LEFT_BODY = Exercise("1", "Left Body")
    private val RIGHT_BODY = Exercise("1", "Right Body")
    private val LEFT_HOOK = Exercise("1", "Left Hook")
    private val RIGHT_HOOK = Exercise("1", "Right Hook")
    private val ALTERNATING_PUNCHES_WITH_SIDE_PUNCH = Exercise("1", "Alternating Punches with Side Punch")

    /*val BOXING_WORKOUT = Workout(arrayListOf(
        Serie.createCycle("Boxing Cycle", arrayListOf(
            CyclicRoutine(LEFT_JAB, 35, 20),
            CyclicRoutine(RIGHT_CROSS, 35, 20),
            CyclicRoutine(LEFT_BODY, 35, 20),
            CyclicRoutine(RIGHT_BODY, 35, 20),
            CyclicRoutine(LEFT_HOOK, 35, 20),
            CyclicRoutine(RIGHT_HOOK, 35, 20),
            CyclicRoutine(ALTERNATING_PUNCHES_WITH_SIDE_PUNCH, 45, 0)),
            60)))*/
  }
}