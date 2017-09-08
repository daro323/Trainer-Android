package com.trainer.modules.training.plan

import android.support.annotation.Keep
import com.trainer.persistence.training.TrainingPlanDao
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Created by dariusz on 30.08.17.
 */
const val PLAN_ID = "planId"

interface TrainingPlanApi {

  @GET("api/plans")
  fun getTrainingPlans(): Single<TrainingPlanListResponse>

  @GET("api/plans/{planId}")
  fun getTrainingPlanDetails(@Path(PLAN_ID) planId: String): Single<TrainingPlanDao.TrainingPlan>

  @PUT("/api/plans/{planId}/current")
  fun setCurrentTrainingPlanId(@Path(PLAN_ID) planId: String): Completable

  @GET("/api/plans/current")
  fun getCurrentTrainingPlanId(): Single<CurrentTrainingPlanIdResponse>





  @Keep
  data class TrainingPlanListResponse(val plans: List<TrainingPlanDao.TrainingPlan>)

  @Keep
  data class TrainingDayResponse(val day: TrainingPlanDao.TrainingDay)

  @Keep
  data class CurrentTrainingPlanIdResponse(val currentPlanId: String)
}