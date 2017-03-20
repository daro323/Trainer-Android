package com.trainer.modules.init.cardioplan.data

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.Workout
import com.trainer.modules.training.types.cyclic.CyclicRoutine

/**
 * Created by dariusz on 20/03/17.
 */
class KettlebellExerciseInitData private constructor() {

  companion object {

    private val DUMMY_KETTLEBELL_EXERCISE = Exercise("Dummy Kettlebell Exercise")

    private val DUMMY_KETTLEBELL_EXERCISE_2 = Exercise("Dummy Kettlebell Exercise 2")

    private val DUMMY_KETTLEBELL_EXERCISE_3 = Exercise("Dummy Kettlebell Exercise 3")


    val KETTLEBELL_WORKOUT = Workout(arrayListOf(
        Serie.createCycle(arrayListOf(
            CyclicRoutine(DUMMY_KETTLEBELL_EXERCISE, 20, 15),
            CyclicRoutine(DUMMY_KETTLEBELL_EXERCISE_2, 30, 10),
            CyclicRoutine(DUMMY_KETTLEBELL_EXERCISE_3, 10, 5)),
            60)
    ))
  }
}