package com.trainer.persistence

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.Keep
import com.trainer.modules.training.Workout

/**
 * Created by dariusz on 29.08.17.
 */
@Keep
@Entity(tableName = TrainingPlanEntity.TABLE_NAME)
data class TrainingPlanEntity(@PrimaryKey val id: Int,
                              val name: String,
                              val categories: kotlin.collections.Set<String>) {
  companion object {
    const val TABLE_NAME = "TrainingPlans"
  }
}

@Keep
@Entity(
    tableName = TrainingDayEntity.TABLE_NAME,
    primaryKeys = arrayOf("category", "trainingPlanId"),
    foreignKeys = arrayOf(ForeignKey(entity = TrainingPlanEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("trainingPlanId"))))
data class TrainingDayEntity(val category: String,
                             val trainingPlanId: Int,
                             val workout: Workout,
                             val totalDone: Int = 0,
                             val lastTrainedDate: String?) {
  companion object {
    const val TABLE_NAME = "TrainingDays"
  }
}