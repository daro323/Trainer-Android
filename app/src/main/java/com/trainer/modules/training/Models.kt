package com.trainer.modules.training

import android.support.annotation.DrawableRes

/**
 * Created by dariusz on 05/01/17.
 */

data class TrainingDay(val name: String,
                       val workout: Workout,
                       val lastWorkout: Workout)

data class Workout(val series: List<Series>)

data class Set(val exercise: Exercise,
               val guidelines: String,
               val seriesCount: Int,
               val progress: List<Repetition>,
               val restTimeSec: Int,
               val comments: String) : Series

data class SuperSet(val seriesList: List<Series>) : Series

data class Exercise(val name: String,
                    val comments: String,
                    @DrawableRes val imageRes: Int)

data class Repetition(val repCount: Int,
                      val weight: String)

interface Series {

}