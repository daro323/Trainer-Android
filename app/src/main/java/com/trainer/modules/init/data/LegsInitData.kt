package com.trainer.modules.init.data

import com.trainer.modules.training.Exercise
import com.trainer.modules.training.Series.Set.Companion.createSet
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 05/01/17.
 */
class LegsInitData private constructor() {

  companion object {
    private val FRONT_SQUAT = Exercise("Przysiad ze sztangą z przodu")

    private val MACHINE_PRESS = Exercise("Wyciskanie na maszynie w górę")

    private val LEG_EXTENSIONS = Exercise("Wyprosty nóg na maszynie")

    private val LEG_CURLS = Exercise("Uginanie nóg na maszynie")

    private val CALF_RAISE = Exercise("Spinanie łydek siedząc")

    val LEGS_WORKOUT = Workout(arrayListOf(
        createSet(FRONT_SQUAT,
            arrayListOf("Zrób 3 serie po 12, 10, 6 repet.", "Po ostatniej od razu max z 80% obciążeniem."),
            4, 100),
        createSet(MACHINE_PRESS,
            arrayListOf("Zrób 3 serie po 10, 10, 8 repet."),
            3, 80),
        createSet(LEG_EXTENSIONS,
            arrayListOf("Zrób 3 serie po 10 repet."),
            3, 60),
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