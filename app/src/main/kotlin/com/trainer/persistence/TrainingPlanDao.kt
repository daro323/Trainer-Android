package com.trainer.persistence

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.trainer.modules.training.TrainingPlan

/**
 * Created by dariusz on 29.08.17.
 */
@Dao
interface TrainingPlanDao {

  @Query("SELECT * FROM ${TrainingPlanEntity.TABLE_NAME}")
  fun listAllPlans(): LiveData<List<TrainingPlanEntity>>

  @Insert
  fun addPlan(plan: TrainingPlan)

  @Delete
  fun removePlan(plan: TrainingPlan)

  @Update
  fun updatePlan(plan: TrainingPlan)
}