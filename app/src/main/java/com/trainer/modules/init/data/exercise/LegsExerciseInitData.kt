package com.trainer.modules.init.data.exercise

import com.trainer.modules.training.Exercise
import com.trainer.modules.training.ExerciseImageMap.*
import com.trainer.modules.training.Series.Companion.createSet
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 05/01/17.
 */
class LegsExerciseInitData private constructor() {

  companion object {
    private val FRONT_SQUAT = Exercise("Przysiad ze sztangą z przodu",
        arrayListOf("Nie zbiegaj kolan środka podczas przysiadu (powinny isć na zewnątrz).",
            "Klata do przodu.",
            "Łokcie do siebie na wprost i do góry."),
        FRONT_SQUAT_IMAGE)

    private val MACHINE_LEG_PRESS = Exercise("Wyciskanie na maszynie w górę",
        arrayListOf("W początkowej pozycji nogi ugięte pod kątem 90 stopni.",
            "Wypychaj ciężar poprzez pięty.",
            "Nie prostuj nóg do końca (niech będą lekko ugięte)."),
        MACHINE_LEG_PRESS_IMAGE)

    private val BULGARIAN_SPLIT_SQUAT = Exercise("Wykroki z oparciem",
        arrayListOf("Wykonuj z hantlami.",
            "Kolano nie może wyprzedzać palców u stopy.",
            "Podczas ruchu kolano musi pozostać w tej samej pozycji.",
            "Tułów prosto, nie pochylaj się do przodu.",
            "Wracając nie cofaj tułowia."),
        BULGARIAN_SPLIT_SQUAT_IMAGE)

    private val LEG_CURLS = Exercise("Uginanie nóg na maszynie",
        arrayListOf("Oparcie ustaw tak, żeby kolano było na wysokości osi obrotu ramienia.",
            "Stopy prosto palce wycelowane w górę."),
        LEG_CURLS_IMAGE)

    private val CALF_RAISE = Exercise("Spinanie łydek siedząc",
        arrayListOf("Wykonuj dokładnie skupiając się na pracy łydek.",
            "Pełny zakres ruchu."),
        CALF_RAISE_IMAGE)




    val LEGS_WORKOUT = Workout(arrayListOf(
        createSet(FRONT_SQUAT,
            arrayListOf("Zrób 4 serie po 10, 8, 7, 6 repet."),
            4, 90),
        createSet(MACHINE_LEG_PRESS,
            arrayListOf("Zrób 4 serie po 10, 8, 7, 7 repet."),
            4, 80),
        createSet(BULGARIAN_SPLIT_SQUAT,
            arrayListOf("Zrób 2 serie po 10 repet."),
            2, 80),
        SuperSet(arrayListOf(
            createSet(LEG_CURLS,
                arrayListOf("Zrób 3 serie po 10 repet."),
                3, 0),
            createSet(CALF_RAISE,
                arrayListOf("Zrób 3 serie po max repet."),
                3, 60)
        ))
    ))
  }
}