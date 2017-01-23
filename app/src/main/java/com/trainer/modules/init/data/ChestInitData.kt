package com.trainer.modules.init.data

import com.trainer.modules.training.Exercise
import com.trainer.modules.training.ExerciseImageMap.*
import com.trainer.modules.training.Series.Set.Companion.createSet
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 05/01/17.
 */
class ChestInitData private constructor() {

  companion object {
    private val BENCH_PRESS = Exercise("Wyciskanie sztangi na ławce",
        arrayListOf("Sztanga na wysokości wzroku.",
            "Łopatki ściągnięte do tyłu i w dół.",
            "Stopy za kolanami.",
            "Nadgarstki trzymaj prosto.",
            "Opuszczaj do kąta prostego, nie niżej.",
            "Opuszczaj powoli, wyciskaj dynamicznie."),
        BENCH_PRESS_IMAGE)

    private val PULL_UPS = Exercise("Podciąganie nachwytem",
        arrayListOf("Drążek musisz minąć brodą.",
            "Ruch zatrzymuj na sekundę na górze.",
            "Nogi skrzyżuj w kostkach i ugnij w kolanach",
            "Ściągaj łopatki.",
            "Opuszczaj się powoli."),
        PULL_UPS_IMAGE,
        BODY_WEIGHT)

    private val INCLINE_DUMBELL_PRESS = Exercise("Wyciskanie hantli na ławce skośnej",
        arrayListOf("Ławka nachylona max 30 stopni.",
            "Klata wypchnięta do przodu.",
            "Hantle sprowadzasz do dolnej części klatki.",
            "Łokcie nie schodzą poniżej 90 stopni."),
        INCLINE_DUMBELL_PRESS_IMAGE)

    private val SINGLE_DUMBELL_ROW = Exercise("Wiosłowanie hantlą",
        arrayListOf("Plecy równoległe do sufitu.",
            "Przyciągaj hantlę do boku klatki piersiowej (do pachy).",
            "Ściągaj łopatki podczas podnoszenia.",
            "Opuszczając nie rosprostuj ręki do końca."),
        SINGLE_DUMBELL_ROW_IMAGE)

    private val CHEST_DIPS = Exercise("Pompki na poręczach",
        arrayListOf("Pochyl głowę i ciało do przodu.",
            "Ściągaj łopatki podczas ruchu."),
        SWEED_PUSHUP_IMAGE,
        BODY_WEIGHT)

    private val DUMBELL_PUSHUP = Exercise("Pompka na hantlach",
        arrayListOf("Po obniżeniu zatrzymaj na sekundę ruch.",
            "Nie pozwól korpusowi na żadną rotację.",
            "Hantlę przyciągnij do boku brzucha.",
            "Jedno powtórzenie to przyciągniecie hantli do lewej i prawej."),
        DUMBELL_PUSHUP_IMAGE)

    private val OVERHEAD_TRICEPS_CABLE_EXTENSIONS = Exercise("Prostowanie przedramion",
        arrayListOf("Wyciąg obniż poniżej linii barków.",
            "Prostuj dynamicznym ruchem."),
        TRICEPS_EXTENSIONS_IMAGE)

    private val TRICEPS_CABLE_PUSHDOWN = Exercise("Ściąganie linek w dół",
        arrayListOf("Łokcie lekko przed biodrami.",
            "Ściągając w dół rób lekki krok do tyłu.",
            "Rozszerzaj linki w końcowej fazie ruchu."),
        TRICEPS_PULLDOWN_IMAGE)

    private val DUMBELL_CURL = Exercise("Zginanie przedramion z hantlami",
        arrayListOf("Stój prosto ruszając tylko przedramionami.",
            "Uginaj do maksymalnego napięcia bicepsa.",
            "Wracaj powoli."),
        DUMBELL_CURL_IMAGE)




    val CHEST_WORKOUT = Workout(arrayListOf(
        SuperSet(arrayListOf(
            createSet(BENCH_PRESS,
                arrayListOf("Zrób 3 serie po 15, 10 i 8 repet.", "Po ostatniej od razu max z 80% obciążeniem."),
                4, 0),
            createSet(PULL_UPS,
                arrayListOf("Zrób 3 serie na max repet."),
                3, 100))
        ),
        SuperSet(arrayListOf(
            createSet(INCLINE_DUMBELL_PRESS,
                arrayListOf("Zrób 3 serie po 15, 10 i 8 repet.", "Po ostatniej od razu max z 80% obciążeniem."),
                4, 0),
            createSet(SINGLE_DUMBELL_ROW,
                arrayListOf("Zrób 3 serie po 15, 12 i 10 repet.", "Po ostatniej od razu max z 80% obciążeniem."),
                4, 80)
        )),
        SuperSet(arrayListOf(
            createSet(CHEST_DIPS,
                arrayListOf("Zrób 2 serie na max repet."),
                2, 0),
            createSet(DUMBELL_PUSHUP,
                arrayListOf("Zrób 2 serie po 10 repet"),
                2, 60))
        ),
        SuperSet(arrayListOf(
            createSet(OVERHEAD_TRICEPS_CABLE_EXTENSIONS,
                arrayListOf("Zrób 3 serie po 15 repet."),
                3, 0),
            createSet(TRICEPS_CABLE_PUSHDOWN,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 0),
            createSet(DUMBELL_CURL,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 60))
        )
    ))
  }
}