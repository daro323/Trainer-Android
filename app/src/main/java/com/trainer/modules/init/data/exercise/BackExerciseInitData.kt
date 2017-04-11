package com.trainer.modules.init.data.exercise

import com.trainer.core.training.model.Exercise
import com.trainer.core.training.model.Serie.Companion.createSet
import com.trainer.core.training.model.WeightType.BODY_WEIGHT
import com.trainer.core.training.model.Workout
import com.trainer.modules.init.InitExerciseImageMap.DEADLIFT_IMAGE
import com.trainer.modules.init.InitExerciseImageMap.PAUSED_PULL_UPS_IMAGE

/**
 * Created by dariusz on 16/01/17.
 */
class BackExerciseInitData private constructor() {

  companion object {
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

    private val V_GRIP_ROW = Exercise("V-Grip Row")

    private val PAUSED_PULL_UPS = Exercise("Podciąganie z przerwą",
        arrayListOf("Drążek musisz minąć brodą.",
            "Ruch zatrzymuj na 10 sekund na górze (z czasem go wydłużaj do 20).",
            "Nogi skrzyżuj w kostkach i ugnij w kolanach.",
            "Ściągaj łopatki.",
            "Chwyt szerszy niż barki.",
            "Opuszczaj się powoli."),
        PAUSED_PULL_UPS_IMAGE,
        BODY_WEIGHT)


    val BACK_WORKOUT = Workout(arrayListOf(
        createSet(DEADLIFT,
            arrayListOf("Zrób 4 serie"),
            4, 100),
        createSet(V_GRIP_ROW,
            arrayListOf("Zrób 3 serie"),
            3, 80),
        createSet(PAUSED_PULL_UPS,
            arrayListOf("Zrób 3 serie po max repet."),
            3, 100)
    ))
  }
}