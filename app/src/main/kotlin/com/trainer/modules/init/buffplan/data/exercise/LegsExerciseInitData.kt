package com.trainer.modules.init.buffplan.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.Workout

/**
 * Created by dariusz on 05/01/17.
 */
class LegsExerciseInitData private constructor() {

  companion object {
    private val FRONT_SQUAT = Exercise("Front Squat",
        arrayListOf(
            "Nie zbiegaj kolan środka podczas przysiadu (powinny isć na zewnątrz).",
            "Klata do przodu.",
            "Łokcie do siebie na wprost i do góry."))

    private val WALKING_LUNGES = Exercise("Walking Lunges")

    private val CALF_RAISE = Exercise("Calf Raise",
        arrayListOf(
            "Wykonuj dokładnie skupiając się na pracy łydek.",
            "Pełny zakres ruchu."))


    val LEGS_WORKOUT = Workout(arrayListOf(
        createSet(FRONT_SQUAT, 4, 100),
        createSet(WALKING_LUNGES, 4, 100),
        createSet(CALF_RAISE, 4, 60)))
  }
}