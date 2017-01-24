package com.trainer.modules.init.data.exercise

import com.trainer.modules.training.Exercise
import com.trainer.modules.training.ExerciseImageMap.*
import com.trainer.modules.training.Series.Set.Companion.createSet
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 16/01/17.
 */
class BackExerciseInitData private constructor() {

  companion object {
    private val KNEELIN_BACK_ROTATIONS = Exercise("Rotacja w klęku podpartym",
        arrayListOf("Dłonie dokładnie pod barkami.",
            "Ściągnij łopatki.",
            "Łokieć kieruj w stronę sufitu.",
            "Wykonuj najpier na jedną stronę, potem na drugą."),
        KNEELIN_BACK_ROTATIONS_IMAGE,
        BODY_WEIGHT)

    private val DEADLIFT = Exercise("Martwy ciąg",
        arrayListOf("Opuszczaj do momentu gdy dojdziesz do połowy golenia.",
            "Nie zaczynaj z przysiadu (biodra wyżej).",
            "Sztanga leży nad centralną częścią stopy.",
            "Klata do przodu i wysoko.",
            "Łopatki ściągnięte w dół i do tyłu (nie unoś ramion).",
            "Minąwszy kolana zacznij przeciągać biodra do przodu.",
            "Kolana nie mogą iść do wewnątrz.",
            "Wypychaj do góry poprzez pięty."),
        DEADLIFT_IMAGE)

    private val DUMBELLS_ROW = Exercise("Wiosłowanie hantlami",
        arrayListOf("Korpus pochylony od 45 do 60 stopni.",
            "Inicjuj ruch ściąganiem łopatek.",
            "Łokcie prowadź szeroko.",
            "Ramiona równoległe do podłoża w końcowej fazie ruchu.",
            "Przyciągaj do dolnej części brzucha."),
        DUMBELLS_ROW_IMAGE)

    private val PAUSED_PULL_UPS = Exercise("Podciąganie z przerwą",
        arrayListOf("Drążek musisz minąć brodą.",
            "Ruch zatrzymuj na 10 sekund na górze (z czasem go wydłużaj do 20).",
            "Nogi skrzyżuj w kostkach i ugnij w kolanach.",
            "Ściągaj łopatki.",
            "Chwyt szerszy niż barki.",
            "Opuszczaj się powoli."),
        PAUSED_PULL_UPS_IMAGE,
        BODY_WEIGHT)

    private val CABLE_ARM_RAISE = Exercise("Unoszenie ramienia po przekątnej",
        arrayListOf("Rękę delikatnie ugnij w łokciu.",
            "Dłoń musi się znaleźć nad głową.",
            "Opuszczaj powoli."),
        CABLE_ARM_RAISE_IMAGE)


    val BACK_WORKOUT = Workout(arrayListOf(
        createSet(KNEELIN_BACK_ROTATIONS,
            arrayListOf("Zrób 2 serie po 20 repet na stronę."),
            2, 40),
        createSet(DEADLIFT,
            arrayListOf("Zrób 4 serie po 8 repet."),
            4, 100),
        createSet(DUMBELLS_ROW,
            arrayListOf("Zrób 3 serie po 10 repet."),
            3, 80),
        createSet(PAUSED_PULL_UPS,
            arrayListOf("Zrób 3 serie po 5 repet."),
            3, 100),
        createSet(CABLE_ARM_RAISE,
            arrayListOf("Zrób 2 serie po 12 repet."),
            2, 60)
    ))
  }
}