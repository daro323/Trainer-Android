package com.trainer.modules.init.data

import com.trainer.R
import com.trainer.modules.training.Exercise
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
        arrayListOf("Opuszczaj powoli, wyciskaj dynamicznie."),
        R.drawable.ex_bench_press)

    private val PULL_UPS = Exercise("Podciąganie nachwytem",
        arrayListOf("Drążek musisz minąć brodą.",
            "Ruch zatrzymuj na sekundę na górze.",
            "Nogi skrzyżuj w kostkach i ugnij w kolanach",
            "Ściągaj łopatki.",
            "Opuszczaj się powoli."),
        R.drawable.ex_pullups,
        BODY_WEIGHT)

    private val INCLINE_DUMBELL_PRESS = Exercise("Wyciskanie hantli na ławce skośnej",
        imageRes = R.drawable.ex_incline_press)

    private val SINGLE_DUMBELL_ROW = Exercise("Wiosłowanie hantlą",
        arrayListOf("Przyciągaj hantlę do boku klatki piersiowej.", "Wykonuj z jedną hantlą na raz."),
        R.drawable.ex_single_dumbell_row)

    private val SWEED_PUSHUP = Exercise("Pompka szwedzka",
        arrayListOf("Wracaj do góry dynamicznym ruchem"),
        R.drawable.ex_sweed_pushup,
        BODY_WEIGHT)

    private val DUMBELL_PUSHUP = Exercise("Pompka na hantlach",
        arrayListOf("Po obniżeniu zatrzymaj na sekundę ruch.",
            "Nie pozwól korpusowi na żadną rotację.",
            "Hantlę przyciągnij do boku brzucha.",
            "Jedno powtórzenie to przyciągniecie hantli do lewej i prawej."),
        R.drawable.ex_dumbell_pushup)

    private val TRICEPS_EXTENSIONS = Exercise("Prostowanie przedramion",
        arrayListOf("Prostuj dynamicznym ruchem."),
        R.drawable.ex_triceps_extensions)

    private val TRICEPS_PULLDOWN = Exercise("Ściąganie linek w dół",
        arrayListOf("Rozszerzaj linki w końcowej fazie ruchu."),
        R.drawable.ex_triceps_pulldown)

    private val DUMBELL_CURL = Exercise("Zginanie przedramion z hantlami",
        arrayListOf("Stój prosto ruszając tylko przedramionami.",
            "Uginaj do maksymalnego napięcia bicepsa.",
            "Wracaj powoli."),
        R.drawable.ex_dumbell_curl)




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
            createSet(SWEED_PUSHUP,
                arrayListOf("Zrób 2 serie na max repet."),
                2, 0),
            createSet(DUMBELL_PUSHUP,
                arrayListOf("Zrób 2 serie po 10 repet"),
                2, 60))
        ),
        SuperSet(arrayListOf(
            createSet(TRICEPS_EXTENSIONS,
                arrayListOf("Zrób 3 serie po 15 repet."),
                3, 0),
            createSet(TRICEPS_PULLDOWN,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 0),
            createSet(DUMBELL_CURL,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 60))
        )
    ))
  }
}