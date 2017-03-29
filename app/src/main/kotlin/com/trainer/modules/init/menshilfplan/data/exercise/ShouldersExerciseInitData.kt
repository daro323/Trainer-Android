package com.trainer.modules.init.menshilfplan.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.Workout
import com.trainer.modules.init.menshilfplan.InitExerciseImageMap.*
import com.trainer.modules.training.types.standard.SuperSet

/**
 * Created by dariusz on 16/01/17.
 */
class ShouldersExerciseInitData private constructor() {

  companion object {
    private val SEATED_DUMBELL_SHOULDER_PRESS = Exercise("Wyciskanie na barki siedząc",
        arrayListOf("Nie zapieraj się głową podczas wyciskania.",
            "Łokcie i ramiona lekko przed sobą (nie cofaj za plecy).",
            "Opuszczaj do 90 stopni w ugięciu ramion."),
        SEATED_DUMBELL_SHOULDER_PRESS_IMAGE)

    private val BARBELL_ROW = Exercise("Wiosłowanie sztangą w opadzie",
        arrayListOf("Sztandze pozwól opadać w prostych rękach.",
            "Przyciągaj do górnej części brzucha.",
            "Opuszczaj dwa razy wolniejszym ruchem."),
        BARBELL_ROW_IMAGE)

    private val DUMBELL_SHOULDER_SIDE_RAISE = Exercise("Unoszenie hantli do boku",
        arrayListOf("Uważaj, żeby nie angażować mięśni karku.",
            "Ściągnij łopatki.",
            "Unoś hantlę na wysokość brody.",
            "Opuszczaj powoli.",
            "Wykonuj jedną hantlą na raz."),
        DUMBELL_SHOULDER_SIDE_RAISE_IMAGE)

    private val DUMBELL_SHOULDER_RAISE = Exercise("Unoszenie hantli na skos",
        arrayListOf("Wykonuj jedną hantlą na raz.",
            "Ściągnij łopatki i wypchnij klatę do przodu.",
            "Nie bujaj tułowiem.",
            "Hantle unieś do przodu i delikatnie na skos ciała.",
            "Opuszczaj powoli."),
        DUMBELL_SHOULDER_RAISE_IMAGE)

    private val CABLE_TO_HEAD_PULL = Exercise("Przyciąganie liny",
        arrayListOf("Wyciąg liny ustawiony na wysokości krtani.",
            "Kciuki skierowane do ciebie.",
            "Ruch inicjuj ściągając łopatki.",
            "Końce liny przeciągnij nad uszy.",
            "Szerokim łukiem prowadź łokcie na boki.",
            "Wracaj powoli."),
        CABLE_TO_HEAD_PULL_IMAGE)

    private val LYING_DUMBELL_ROTATIONS = Exercise("Unoszenie hantli w leżeniu bokiem",
        arrayListOf("Stopy trzymaj razem, jedna na drugiej.",
            "Łokieć i ramię dociśnij do korpusu (swobodnie).",
            "Ruch wykonuj dokładnie i spokojnie."),
        LYING_DUMBELL_ROTATIONS_IMAGE)


    val SHOULDERS_WORKOUT = Workout(arrayListOf(
        SuperSet(arrayListOf(
            createSet(SEATED_DUMBELL_SHOULDER_PRESS,
                arrayListOf("Zrób 4 serie po 8, 8, 7, 6 repet."),
                4, 0),
            createSet(BARBELL_ROW,
                arrayListOf("Zrób 4 serie po 8, 8, 7, 6 repet."),
                4, 80)
        )),
        SuperSet(arrayListOf(
            createSet(DUMBELL_SHOULDER_SIDE_RAISE,
                arrayListOf("Zrób 3 serie po 8, 7, 6 repet."),
                3, 0),
            createSet(DUMBELL_SHOULDER_RAISE,
                arrayListOf("Zrób 3 serie po 8, 7, 6 repet."),
                3, 80)
        )),
        SuperSet(arrayListOf(
            createSet(CABLE_TO_HEAD_PULL,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 0),
            createSet(LYING_DUMBELL_ROTATIONS,
                arrayListOf("Zrób 3 serie po 10 repet na obie strony."),
                3, 70)
        ))
    ))
  }
}