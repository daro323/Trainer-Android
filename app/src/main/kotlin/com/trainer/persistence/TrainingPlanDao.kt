package com.trainer.persistence

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

/**
 * Created by dariusz on 29.08.17.
 */
@Dao
interface TrainingPlanDao {

  @Query("SELECT * FROM TrainingPlans")
  fun listAllPlans(): LiveData<List<TrainingPlanEntity>>

  @Insert
  fun addPlan(planEntity: TrainingPlanEntity)

  @Delete
  fun removePlan(planEntity: TrainingPlanEntity)

  @Update
  fun updatePlan(planEntity: TrainingPlanEntity)
}