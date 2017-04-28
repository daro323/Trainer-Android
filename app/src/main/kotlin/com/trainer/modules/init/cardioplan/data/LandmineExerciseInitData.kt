package com.trainer.modules.init.cardioplan.data

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.Workout
import com.trainer.modules.training.types.cyclic.CyclicRoutine
import com.trainer.modules.training.types.standard.SuperSet

/**
 * Created by dariusz on 20/03/17.
 */
class LandmineExerciseInitData private constructor() {

  companion object {

    private val DUMMY_LANDMINE_EXERCISE = Exercise("Dummy LANDMINE Exercise")

    private val DUMMY_LANDMINE_EXERCISE_2 = Exercise("Dummy LANDMINE Exercise 2")

    private val DUMMY_LANDMINE_EXERCISE_3 = Exercise("Dummy LANDMINE Exercise 3")


    private val DUMMY_SET = Exercise("Dummy Set Exercise")

    private val DUMMY_SET_2 = Exercise("Dummy Set Exercise 2")

    private val DUMMY_SET_3 = Exercise("Dummy Set Exercise 3")


    val LANDMINE_WORKOUT = Workout(arrayListOf(
        Serie.createCycle("Dummy Landmine cycle", arrayListOf(
            CyclicRoutine(DUMMY_LANDMINE_EXERCISE, 2, 2),
            CyclicRoutine(DUMMY_LANDMINE_EXERCISE_2, 2, 2)),
            2),

        Serie.createSet(DUMMY_SET, emptyList(), 2, 2),

        SuperSet(arrayListOf(
            Serie.createSet(DUMMY_SET_2, emptyList(), 2, 0),
            Serie.createSet(DUMMY_SET_3, emptyList(), 2, 2)
        ))
    ))
  }
}