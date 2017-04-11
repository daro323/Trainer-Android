package com.trainer.modules.init.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.WeightType.BODY_WEIGHT
import com.trainer.core.training.model.Workout
import com.trainer.modules.init.InitExerciseImageMap.*
import com.trainer.modules.training.standard.SuperSet

/**
 * Created by dariusz on 05/01/17.
 */
class ChestExerciseInitData private constructor() {

  companion object {
    private val BENCH_PRESS = Exercise("Wyciskanie sztangi na ławce",
        arrayListOf("Sztanga na wysokości wzroku.",
            "Łopatki ściągnięte do tyłu i w dół.",
            "Stopy za kolanami.",
            "Nadgarstki trzymaj prosto.",
            "Opuszczaj do kąta prostego, nie niżej.",
            "Opuszczaj powoli, wyciskaj dynamicznie."),
        BENCH_PRESS_IMAGE)

    private val INCLINE_DUMBELL_PRESS = Exercise("Wyciskanie hantli na ławce skośnej",
        arrayListOf("Ławka nachylona do max 30 stopni.",
            "Klata wypchnięta do przodu.",
            "Hantle sprowadzasz do dolnej części klatki.",
            "Łokcie nie schodzą poniżej 90 stopni."),
        INCLINE_DUMBELL_PRESS_IMAGE)

    private val WEIGHTED_DIPS = Exercise("Pompki na poręczach",
        arrayListOf("Pochyl głowę i ciało do przodu.",
            "Ściągaj łopatki podczas ruchu."),
        CHEST_DIPS_IMAGE,
        BODY_WEIGHT)






    val CHEST_WORKOUT = Workout(arrayListOf(
        createSet(BENCH_PRESS,
            arrayListOf("Zrób 3 serie"),
            3, 100),
        createSet(INCLINE_DUMBELL_PRESS,
            arrayListOf("Zrób 3 serie"),
            3, 80),
        createSet(WEIGHTED_DIPS,
            arrayListOf("Zrób 3 serie"),
            3, 0),
        SuperSet(arrayListOf(
            createSet(OVERHEAD_TRICEPS_CABLE_EXTENSIONS,
                arrayListOf("Zrób 2 serie po 10 repet."),
                2, 0),
            createSet(TRICEPS_CABLE_PUSHDOWN,
                arrayListOf("Zrób 2 serie po 8 repet."),
                2, 0),
            createSet(SUPINATED_BICEPS_CURL,
                arrayListOf("Zrób 2 serie po 10, 8 repet."),
                2, 70))
        )
    ))
  }
}