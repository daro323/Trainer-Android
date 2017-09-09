package com.trainer.modules.training.plan

import com.trainer.persistence.training.TrainingPlanDao
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by dariusz on 30.08.17.
 */
const val PLAN_ID = "planId"

interface TrainingPlanApi {

  @GET("/api/plans")
  fun getTrainingPlans(): Single<TrainingPlanListResponse>

  @GET("/api/plans/{planId}")
  fun getTrainingPlanDetails(@Path(PLAN_ID) planId: String): Single<TrainingPlanDao.TrainingPlan>

  @POST("/api/plans/current")
  fun setCurrentTrainingPlanId(@Body body: CurrentTrainingPlanInfo): Completable

  @GET("/api/plans/current")
  fun getCurrentTrainingPlanId(): Single<CurrentTrainingPlanInfo>
}