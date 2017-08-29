package com.trainer.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

/**
 * Created by dariusz on 29.08.17.
 */
@Database(
    version = 1,
    entities = arrayOf(TrainingPlanEntity::class, TrainingDayEntity::class))
@TypeConverters(Converters::class)
abstract class TrainingDatabase : RoomDatabase() {

  abstract fun trainingPlanDao(): TrainingPlanDao
}