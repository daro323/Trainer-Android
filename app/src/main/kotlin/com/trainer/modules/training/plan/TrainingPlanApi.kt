package com.trainer.modules.training.plan

import android.support.annotation.Keep
import com.trainer.modules.training.workout.TrainingDay
import com.trainer.modules.training.workout.TrainingPlan
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by dariusz on 30.08.17.
 */
interface TrainingPlanApi {

  @GET("api/plans")
  fun getTrainingPlans(): Single<TrainingPlanListResponse>





  @Keep
  data class TrainingPlanListResponse(val plans: List<TrainingPlan>)

  @Keep
  data class TrainingDayResponse(val day: TrainingDay)
}