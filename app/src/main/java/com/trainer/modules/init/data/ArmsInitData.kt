package com.trainer.modules.init.data

import com.trainer.modules.training.Exercise
import com.trainer.modules.training.ExerciseImageMap.*
import com.trainer.modules.training.Series.Set.Companion.createSet
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 16/01/17.
 */
class ArmsInitData private constructor() {

  companion object {
    private val NARROW_GRIP_PULLUP = Exercise("Podciąganie wąskim podchwytem",
        arrayListOf("Podciągaj się aż miniesz drążek głową.",
            "Przytrzymaj podciągnięcie na 3 sekundy."),
        NARROW_GRIP_PULLUP_IMAGE,
        BODY_WEIGHT)

    private val TRICEPS_DIPS = Exercise("Pompki na poręczach na triceps",
        arrayListOf("Łokcie blisko tułowia.",
            "Tułów i głowa prosto."),
        CHEST_DIPS_IMAGE,
        BODY_WEIGHT)

    private val SEATED_BARBELL_SHOULDER_PRESS = Exercise("Wyciskanie sztangi siedząc",
        arrayListOf("Mocno chwytaj sztangę.",
            "Nadgarstki powinny być w linii z łokciami.",
            "Dozwolone jest jedynie lekkie wygięcie pleców."),
        SEATED_BARBELL_SHOULDER_PRESS_IMAGE)

    private val BARBELL_BENT_OVER_ROW = Exercise("Wiosłowanie sztangą w opadzie",
        arrayListOf("Pochyl się od 45 do 60 stopni.",
            "Tułów stabilny bez ruchów.",
            "Sztandze pozwól opadać w prostych rękach.",
            "Przyciągaj do górnej części brzucha.",
            "Opuszczaj dwa razy wolniejszym ruchem."),
        BARBELL_ROW_IMAGE)

    private val BARBELL_BICEPS_CURL = Exercise("Uginanie przedramion",
        arrayListOf("Podnoś do maksymalnego spięcia bicepsa.",
            "W końcowej fazie unieś łokcie.",
            "Opuszczaj bardzo bardzo powoli.",
            "Opuszczaj do pełnego wyprostu."),
        BARBELL_BICEPS_CURL_IMAGE)

    private val LEANING_TRICEPS_CABLE_EXTENSIONS = Exercise("Prostowanie przedramion w pochyleniu na wyciągu",
        arrayListOf("Wracaj bardzo bardzo powoli."))


    val ARMS_WORKOUT = Workout(arrayListOf(
        SuperSet(arrayListOf(
            createSet(NARROW_GRIP_PULLUP,
                arrayListOf("Zrób 4 serie po kolejno 8, 6, 5 i max repet."),
                4, 0),
            createSet(TRICEPS_DIPS,
                arrayListOf("Zrób 4 serie po kolejno 8, 6, 5 i max repet."),
                4, 80)
        )),
        SuperSet(arrayListOf(
            createSet(SEATED_BARBELL_SHOULDER_PRESS,
                arrayListOf("Zrób 3 serie po 8 repet."),
                3, 0),
            createSet(BARBELL_BENT_OVER_ROW,
                arrayListOf("Zrób 3 serie po 8 repet."),
                3, 90)
        )),
        SuperSet(arrayListOf(
            createSet(BARBELL_BICEPS_CURL,
                arrayListOf("Zrób 3 serie po 8 repet."),
                3, 0),
            createSet(LEANING_TRICEPS_CABLE_EXTENSIONS,
                arrayListOf("Zrób 3 serie po 8 repet."),
                3, 80)
        ))
    ))
  }
}