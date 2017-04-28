package com.trainer.modules.init.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.Workout

/**
 * Created by dariusz on 16/01/17.
 */
class BackExerciseInitData private constructor() {

  companion object {
    private val DEADLIFT = Exercise("Deadlift",
        arrayListOf(
            "Opuszczaj do momentu gdy dojdziesz do połowy golenia.",
            "Nie zaczynaj z przysiadu (biodra wyżej).",
            "Sztanga leży nad centralną częścią stopy.",
            "Klata do przodu i wysoko.",
            "Łopatki ściągnięte w dół i do tyłu (nie unoś ramion).",
            "Minąwszy kolana zacznij przeciągać biodra do przodu.",
            "Kolana nie mogą iść do wewnątrz.",
            "Wypychaj do góry poprzez pięty."))

    private val V_GRIP_ROW = Exercise("V-Grip Row")

    private val WEIGHTED_PULL_UPS = Exercise("Pull-Ups",
        arrayListOf(
            "Drążek musisz minąć brodą.",
            "Ściągaj łopatki.",
            "Chwyt szerszy niż barki.",
            "Opuszczaj się powoli."))

    private val BENT_OVER_ROW = Exercise("Bent Over Row",
        arrayListOf(
            "Pochyl się od 45 do 60 stopni.",
            "Tułów stabilny bez ruchów.",
            "Sztandze pozwól opadać w prostych rękach.",
            "Przyciągaj do górnej części brzucha.",
            "Łokcie lekko na boki.",
            "Opuszczaj dwa razy wolniejszym ruchem.",
            "Ściagaj łopatki na koniec ruchu."))

    private val STRAIGHT_ARM_PULLDOWNS = Exercise("Straight Arm Pulldowns",
        arrayListOf(
            "Ściągaj łopatki wraz ze ściąganiem drążka w dół."))


    val BACK_WORKOUT = Workout(arrayListOf(
        createSet(DEADLIFT, 4, 100),
        createSet(BENT_OVER_ROW, 3, 90),
        createSet(WEIGHTED_PULL_UPS, 3, 90),
        createSet(V_GRIP_ROW, 3, 80),
        createSet(STRAIGHT_ARM_PULLDOWNS, 2, 70)
    ))
  }
}