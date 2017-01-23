package com.trainer.modules.init.data

import com.trainer.modules.training.Exercise
import com.trainer.modules.training.ExerciseImageMap.*
import com.trainer.modules.training.Series.Set.Companion.createSet
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 05/01/17.
 */
class LegsInitData private constructor() {

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

    private val DUMBELL_WALKING_LUNGES = Exercise("Wykroki z hantlami",
        arrayListOf("Wykrok musi być na tyle duży żeby uzyskać 90 stopni.",
            "Kolano nie może wyprzedzać palców stopy.",
            "Ręce prosto, wzdłuż ciała.",
            "Wykroki rób na zmianę."),
        DUMBELL_WALKING_LUNGES_IMAGE)

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
            arrayListOf("Zrób 4 serie po 12, 10, 8, 6 repet."),
            4, 90),
        createSet(MACHINE_LEG_PRESS,
            arrayListOf("Zrób 3 serie po 10, 10, 8 repet."),
            3, 80),
        createSet(DUMBELL_WALKING_LUNGES,
            arrayListOf("Zrób 3 serie po 10 repet."),
            3, 80),
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