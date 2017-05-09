package com.trainer.modules.init.buffplan.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.Workout
import com.trainer.modules.training.types.standard.SuperSet

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

    private val MACHINE_LEG_PRESS = Exercise("Wyciskanie na maszynie w górę",
        arrayListOf(
            "W początkowej pozycji nogi ugięte pod kątem 90 stopni.",
            "Wypychaj ciężar poprzez pięty.",
            "Nie prostuj nóg do końca (niech będą lekko ugięte)."))

    private val ROMANIAN_DEADLIFT = Exercise("Romanian Deadlift",
        arrayListOf(
            "Chwyć nachwytem na szerokość barków.",
            "Opuszczaj bardzo powoli.",
            "Ruch podobny do martwego tyle, że bez przysiadu.",
            "Gdy maxymalnie rozciągniesz tyłek wykonaj dynamiczny powrót."))

    private val LEG_CURLS = Exercise("Leg Curls",
        arrayListOf(
            "Oparcie ustaw tak, żeby kolano było na wysokości osi obrotu ramienia.",
            "Stopy prosto palce wycelowane w górę."))

    private val CALF_RAISE = Exercise("Calf Raise",
        arrayListOf(
            "Wykonuj dokładnie skupiając się na pracy łydek.",
            "Pełny zakres ruchu."))


    val LEGS_WORKOUT = Workout(arrayListOf(
        createSet(FRONT_SQUAT, 4, 100),
        createSet(WALKING_LUNGES, 3, 100),
        createSet(MACHINE_LEG_PRESS, 3, 90),
        createSet(ROMANIAN_DEADLIFT, 2, 75),
        SuperSet("Leg Curls & Calf Raises",
            arrayListOf(
                createSet(LEG_CURLS, 3, 0),
                createSet(CALF_RAISE, 3, 60)
            ))
    ))
  }
}