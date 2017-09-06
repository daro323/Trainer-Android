package com.trainer.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.trainer.modules.training.workout.Exercise
import com.trainer.modules.training.workout.TrainingDay
import com.trainer.modules.training.workout.TrainingPlan

/**
 * Created by dariusz on 29.08.17.
 */
@Database(
    version = 1,
    entities = arrayOf(
        TrainingPlan::class,
        TrainingDay::class,
        Exercise::class))
@TypeConverters(Converters::class)
abstract class TrainingDatabase : RoomDatabase() {

  abstract fun trainingPlanDao(): TrainingPlanDao
}