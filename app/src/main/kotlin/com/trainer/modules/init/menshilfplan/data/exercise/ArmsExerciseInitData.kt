package com.trainer.modules.init.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.Workout
import com.trainer.modules.training.types.standard.SuperSet

/**
 * Created by dariusz on 16/01/17.
 */
class ArmsExerciseInitData private constructor() {

  companion object {
    private val WEIGHTED_CHINUPS = Exercise("Weighted Chin-Ups",
        arrayListOf(
            "Chwyt na szerokość ramion.",
            "Podciągaj się aż broda minie drążek.",
            "Przytrzymaj podciągnięcie na 3 sekundy.",
            "Ruch wykonuj powoli."))

    private val BARBELL_CURLS = Exercise("Barbell Curls",
        arrayListOf(
            "Chwyt na szerokość ramion lub lekko szerzej",
            "Podnoś do maksymalnego spięcia bicepsa.",
            "Opuszczaj bardzo bardzo powoli.",
            "Opuszczaj do pełnego wyprostu."))

    private val SUPINATED_DUMBBELL_CURLS = Exercise("Supinated Dumbbell Curls",
        arrayListOf(
            "Chwyć hantle na skraju zewnętrznej strony (trudniejsza supinacja).",
            "Skręcanie nadgarsteka i uginanie ramion jako jeden ruch.",
            "Uginaj do maksymalnego napięcia bicepsa.",
            "Wracaj bardzo powoli."))

    private val HAMMER_CURLS = Exercise("Hammer Curls",
        arrayListOf(
            "Ramiona ściągnięte do tyłu."))


    private val WEIGHTED_TRICEPS_DIPS = Exercise("Weighted Dips",
        arrayListOf(
            "Łokcie blisko tułowia.",
            "Tułów i głowa prosto."))

    private val CLOSE_GRIP_BENCH = Exercise("Close Grip Bench",
        arrayListOf(
            "Chwyt troche węższy niż szerokość ramion.",
            "Łokcie blisko tułowia.",
            "Tułów i głowa prosto."))

    private val OVERHEAD_TRICEPS_CABLE_EXTENSIONS = Exercise("Overhead Cable Extensions",
        arrayListOf(
            "Wyciąg obniż poniżej linii barków.",
            "Prostuj dynamicznym ruchem."))

    private val TRICEPS_CABLE_PUSHDOWN = Exercise("Cable Extensions",
        arrayListOf(
            "Łokcie lekko przed biodrami.",
            "Ściągając w dół rób lekki krok do tyłu.",
            "Rozszerzaj linki w końcowej fazie ruchu."))


    val ARMS_WORKOUT = Workout(arrayListOf(
        SuperSet(
            arrayListOf(
                createSet(WEIGHTED_CHINUPS, 3, 0),
                createSet(WEIGHTED_TRICEPS_DIPS, 3, 75))),

        SuperSet(
            arrayListOf(
                createSet(BARBELL_CURLS, 3, 0),
                createSet(OVERHEAD_TRICEPS_CABLE_EXTENSIONS, 3, 75))),

        SuperSet(
            arrayListOf(
                createSet(SUPINATED_DUMBBELL_CURLS, 2, 0),
                createSet(CLOSE_GRIP_BENCH, 2, 75))),

        SuperSet(
            arrayListOf(
                createSet(HAMMER_CURLS, 2, 0),
                createSet(TRICEPS_CABLE_PUSHDOWN, 2, 75)))
    ))
  }
}