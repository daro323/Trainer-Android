package com.trainer.modules.init.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.Workout

/**
 * Created by dariusz on 05/01/17.
 */
class ChestExerciseInitData private constructor() {

  companion object {
    private val BENCH_PRESS = Exercise("Bench Press",
        arrayListOf(
            "Sztanga na wysokości wzroku.",
            "Łopatki ściągnięte do tyłu i w dół.",
            "Stopy za kolanami.",
            "Nadgarstki trzymaj prosto.",
            "Opuszczaj do kąta prostego, nie niżej.",
            "Opuszczaj powoli, wyciskaj dynamicznie."))

    private val INCLINE_DUMBELL_PRESS = Exercise("Incline Dumbell Press",
        arrayListOf(
            "Ławka nachylona do max 30 stopni.",
            "Klata wypchnięta do przodu.",
            "Hantle sprowadzasz do dolnej części klatki.",
            "Łokcie nie schodzą poniżej 90 stopni."))

    private val WEIGHTED_CHEST_DIPS = Exercise("Weighted Chest Dips",
        arrayListOf(
            "Pochyl głowę i ciało do przodu.",
            "Ściągaj łopatki podczas ruchu."))

    private val CABLE_CROSSOVER_CHEST_PRESS = Exercise("Cable Crossover Chest Press",
        arrayListOf(
            "Łokieć przy ciele"))



    val CHEST_WORKOUT = Workout(arrayListOf(
        createSet(BENCH_PRESS, 4, 100),
        createSet(INCLINE_DUMBELL_PRESS, 4, 90),
        createSet(WEIGHTED_CHEST_DIPS, 3, 90),
        createSet(CABLE_CROSSOVER_CHEST_PRESS, 3, 75)
    ))
  }
}