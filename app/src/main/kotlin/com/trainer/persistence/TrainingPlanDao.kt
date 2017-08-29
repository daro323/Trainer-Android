package com.trainer.persistence

import android.arch.persistence.room.*
import com.trainer.core.training.model.TrainingPlan

/**
 * Created by dariusz on 29.08.17.
 */
@Dao
interface TrainingPlanDao {

  @Query("SELECT * FROM ${TrainingPlanEntity.TABLE_NAME}")
  fun listAllPlans(): List<TrainingPlanEntity>

  @Insert
  fun addPlan(plan: TrainingPlan)

  @Delete
  fun removePlan(plan: TrainingPlan)

  @Update
  fun updatePlan(plan: TrainingPlan)
}