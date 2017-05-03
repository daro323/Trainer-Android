package com.trainer.modules.init.menshilfplan.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.Workout
import com.trainer.modules.training.types.standard.SuperSet

/**
 * Created by dariusz on 16/01/17.
 */
class ShouldersExerciseInitData private constructor() {

  companion object {
    private val STANDING_OVERHEAD_PRESS = Exercise("Standing Overhead Press")

    private val BARBELL_ROW = Exercise("Wiosłowanie sztangą w opadzie",
        arrayListOf("Sztandze pozwól opadać w prostych rękach.",
            "Przyciągaj do górnej części brzucha.",
            "Opuszczaj dwa razy wolniejszym ruchem."))

    private val LATERAL_DUMBBELL_RAISE = Exercise("Lateral Dumbbell Raise",
        arrayListOf(
            "Uważaj, żeby nie angażować mięśni karku.",
            "Prowadź łokciem.",
            "Ściągnij łopatki.",
            "Unoś hantlę na wysokość brody.",
            "Opuszczaj powoli.",
            "Wykonuj jedną hantlą na raz."))

    private val FRONT_ACROSS_DUMBBELL_RAISE = Exercise("Front & Across Dumbbell Raise",
        arrayListOf(
            "Wykonuj jedną hantlą na raz.",
            "Ściągnij łopatki i wypchnij klatę do przodu.",
            "Nie bujaj tułowiem.",
            "Hantle unieś do przodu i delikatnie na skos ciała.",
            "Opuszczaj powoli."))

    private val CABLE_TO_HEAD_PULL = Exercise("Cable to Head Pull",
        arrayListOf(
            "Wyciąg liny ustawiony na wysokości krtani.",
            "Kciuki skierowane do ciebie.",
            "Ruch inicjuj ściągając łopatki.",
            "Końce liny przeciągnij nad uszy.",
            "Szerokim łukiem prowadź łokcie na boki.",
            "Wracaj powoli."))

    private val LYING_DUMBBELL_ROTATIONS = Exercise("Lying Dumbbell Rotations",
        arrayListOf(
            "Stopy trzymaj razem, jedna na drugiej.",
            "Łokieć i ramię dociśnij do korpusu (swobodnie).",
            "Ruch wykonuj dokładnie i spokojnie."))

    private val TRAPS_ROTATE_AND_PULL = Exercise("Traps Rotate & Pull")

    private val TRAPS_PLATE_RAISE = Exercise("Traps Plate Raise",
        arrayListOf(
            "Podnoś talerz od pozycji poziomej w górę."
        ))


    val SHOULDERS_WORKOUT = Workout(arrayListOf(
        createSet(STANDING_OVERHEAD_PRESS, 3, 80),
        createSet(LATERAL_DUMBBELL_RAISE, 3, 80),
        createSet(FRONT_ACROSS_DUMBBELL_RAISE, 3, 80),
        SuperSet("Cable to Head Pulls & Lying Dumbbell Rotations",
            arrayListOf(
                createSet(CABLE_TO_HEAD_PULL, 3, 0),
                createSet(LYING_DUMBBELL_ROTATIONS, 3, 70)
            )),

        SuperSet("Traps Rotate Pull & Traps Plate Raise",
            arrayListOf(
                createSet(TRAPS_ROTATE_AND_PULL, 2, 0),
                createSet(TRAPS_PLATE_RAISE, 2, 60)
            ))
    ))
  }
}