package com.trainer.modules.init.data

import com.trainer.R
import com.trainer.modules.training.Exercise
import com.trainer.modules.training.Series.Set.Companion.createSet
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 16/01/17.
 */
class BackInitData private constructor() {

  companion object {
    private val KNEELIN_BACK_ROTATIONS = Exercise("Rotacja w klęku podpartym",
        arrayListOf("Dłonie dokładnie pod barkami.",
            "Ściągnij łopatki.",
            "Łokieć kieruj w stronę sufitu.",
            "Wykonuj najpier na jedną stronę, potem na drugą."),
        R.drawable.ex_kneeling_back_rotations,
        BODY_WEIGHT)

    private val DEADLIFT = Exercise("Martwy ciąg",
        imageRes = R.drawable.ex_deadlift)

    private val DUMBELLS_ROW = Exercise("Wiosłowanie hantlami",
        arrayListOf("Korpus niemal równoległy z podłogą.",
            "Inicjuj ruch ściąganiem łopatek.",
            "Łokcie prowadź szeroko.",
            "Ramiona równoległe do podłoża w końcowej fazie ruchu."),
        R.drawable.ex_dumbells_row)

    private val PAUSED_PULL_UPS = Exercise("Podciąganie z przerwą",
        arrayListOf("Drążek musisz minąć brodą.",
            "Ruch zatrzymuj na 10 sekund na górze (z czasem go wydłużaj do 20).",
            "Nogi skrzyżuj w kostkach i ugnij w kolanach",
            "Ściągaj łopatki.",
            "Opuszczaj się powoli."),
        R.drawable.ex_pullups,
        BODY_WEIGHT)

    private val CABLE_ARM_RAISE = Exercise("Unoszenie ramienia po przekątnej",
        arrayListOf("Rękę delikatnie ugnij w łokciu.",
            "Dłoń musi się znaleźć nad głową."),
        R.drawable.ex_cable_arm_raise)


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