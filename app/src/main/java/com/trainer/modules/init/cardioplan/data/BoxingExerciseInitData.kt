package com.trainer.modules.init.cardioplan.data

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.Workout
import com.trainer.modules.training.types.cyclic.CyclicRoutine

/**
 * Created by dariusz on 20/03/17.
 */
class BoxingExerciseInitData private constructor() {

  companion object {

    private val DUMMY_BOXING_EXERCISE = Exercise("Dummy BOXING Exercise")

    private val DUMMY_BOXING_EXERCISE_2 = Exercise("Dummy BOXING Exercise 2")

    private val DUMMY_BOXING_EXERCISE_3 = Exercise("Dummy BOXING Exercise 3")


    val BOXING_WORKOUT = Workout(arrayListOf(
        Serie.createCycle("Dummy Boxing cycle", arrayListOf(
            CyclicRoutine(DUMMY_BOXING_EXERCISE, 20, 15),
            CyclicRoutine(DUMMY_BOXING_EXERCISE_2, 30, 10),
            CyclicRoutine(DUMMY_BOXING_EXERCISE_3, 10, 5)),
            60)
    ))
  }
}