package com.trainer.modules.init.data

import com.trainer.R
import com.trainer.modules.training.Exercise
import com.trainer.modules.training.Series.Set.Companion.createSet
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 16/01/17.
 */
class ShouldersInitData private constructor() {

  companion object {
    private val SEATED_DUMBELL_SHOULDER_PRESS = Exercise("Wyciskanie na barki siedząc",
        imageRes = R.drawable.ex_dumbell_shoulder_press)

    private val BARBELL_ROW = Exercise("Wiosłowanie sztangą w opadzie",
        arrayListOf("Sztandze pozwól opadać w prostych rękach.",
            "Przyciągaj do górnej części brzucha.",
            "Opuszczaj dwa razy wolniejszym ruchem."),
        R.drawable.ex_barbell_row)

    private val BARBELL_TO_CHEST_PULL = Exercise("Ściąganie drążka do klatki podchwytem",
        arrayListOf("Złap podchwytem na szerokość barków.",
            "Delikatnie odchyl się do tyłu.",
            "Przyciągaj do góry klatki piersiowej.",
            "Zatrzymaj na sekundę ruch w końcowej fazie.",
            "Opuszczaj powoli.",
            "Pozostań w odchyleniu nie ruszając tułowiem."),
        R.drawable.ex_barbell_to_chest_pull)

    private val DUMBELL_SHOULDER_RAISE = Exercise("Unoszenie hantli",
        arrayListOf("Ściągnij łopatki i wypchnij klatę do przodu.",
            "Nie bujaj tułowiem.",
            "Hantle unieś do przodu i delikatnie na boki.",
            "W końcowej fazie ręce równoległe do podłoża.",
            "Opuszczaj powoli."),
        R.drawable.ex_dumbell_shoulder_raise)

    private val CABLE_TO_HEAD_PULL = Exercise("Przyciąganie liny",
        arrayListOf("Wyciąg liny ustawiony na wysokości oczu.",
            "Ruch inicjuj ściągając łopatki.",
            "Końce liny przeciągnij za uszy.",
            "Szerokim łukiem prowadź łokcie na boki.",
            "Wracaj powoli."),
        R.drawable.ex_cable_to_head_pull)

    private val LYING_DUMBELL_ROTATIONS = Exercise("Unoszenie hantli w leżeniu bokiem",
        arrayListOf("Stopy trzymaj razem, jedna na drugiej.",
            "Łokieć i ramię dociśnij do korpusu (swobodnie).",
            "Ruch wykonuj dokładnie i spokojnie."),
        R.drawable.ex_lying_dumbell_rotations)


    val SHOULDERS_WORKOUT = Workout(arrayListOf(
        SuperSet(arrayListOf(
            createSet(SEATED_DUMBELL_SHOULDER_PRESS,
                arrayListOf("Zrób 4 serie po 8 repet."),
                4, 0),
            createSet(BARBELL_ROW,
                arrayListOf("Zrób 4 serie po 8 repet."),
                4, 80)
        )),
        SuperSet(arrayListOf(
            createSet(BARBELL_TO_CHEST_PULL,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 0),
            createSet(DUMBELL_SHOULDER_RAISE,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 80)
        )),
        SuperSet(arrayListOf(
            createSet(CABLE_TO_HEAD_PULL,
                arrayListOf("Zrób 3 serie po 12 repet."),
                3, 0),
            createSet(LYING_DUMBELL_ROTATIONS,
                arrayListOf("Zrób 3 serie po 15 repet na obie strony."),
                3, 70)
        ))
    ))
  }
}