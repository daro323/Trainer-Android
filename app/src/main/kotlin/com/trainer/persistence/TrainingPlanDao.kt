package com.trainer.persistence

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.trainer.modules.training.workout.TrainingPlan
import io.reactivex.Flowable

/**
 * Created by dariusz on 29.08.17.
 */
@Dao
interface TrainingPlanDao {

  @Query("SELECT * FROM TrainingPlans")
  fun listAllPlansLD(): LiveData<List<TrainingPlan>>

  @Query("SELECT * FROM TrainingPlans")
  fun listAllPlans(): Flowable<TrainingPlan>

  @Insert
  fun addPlan(planEntity: TrainingPlan)

  @Insert
  fun addPlans(plansEntityList: List<TrainingPlan>)

  @Delete
  fun removePlan(planEntity: TrainingPlan)

  @Update
  fun updatePlan(planEntity: TrainingPlan)
}