package com.trainer.modules.init.cardioplan.data

import com.trainer.persistence.training.TrainingPlanDao.Exercise

/**
 * Created by dariusz on 20/03/"1"7.
 */
class KettlebellExerciseInitData private constructor() {

  companion object {

    private val SWING = Exercise("1", "Swing")
    private val SQUAT_PRESS_LEFT = Exercise("1", "Squat & Press (Left Arm)")
    private val SWING_BETWEEN_LEGS = Exercise("1", "Swing Between Legs")
    private val SQUAT_PRESS_RIGTH = Exercise("1", "Squat & Press (Right Arm)")
    private val HALO = Exercise("1", "Halo")
    private val LUNGE_PASS = Exercise("1", "Lunge Pass")
    private val HIGH_PULL = Exercise("1", "High Pull")
    private val GOBLET_SQUAT_WITH_ABDUCT_LEFT = Exercise("1", "Goblet Squat (Abduct Left)")
    private val SNATCH = Exercise("1", "Snatch")
    private val GOBLET_SQUAT_WITH_ABDUCT_RIGHT = Exercise("1", "Goblet Squat (Abduct Right)")
    private val SIDE_CHOP_LEFT = Exercise("1", "Side Chop (Left)")
    private val RUSSIAN_TWIST = Exercise("1", "Russian Twist")
    private val SIDE_CHOP_RIGHT = Exercise("1", "Side Chop (Right)")


    /*val KETTLEBELL_WORKOUT = Workout(arrayListOf(
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
            60)))*/
  }
}