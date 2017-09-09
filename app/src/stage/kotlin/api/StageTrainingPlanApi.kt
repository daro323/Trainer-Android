package api

import com.trainer.modules.training.plan.CurrentTrainingPlanInfo
import com.trainer.modules.training.plan.TrainingPlanListResponse
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

interface StageTrainingPlanApi {

  @GET("/plans")
  fun getTrainingPlans(): Single<TrainingPlanListResponse>

  @GET("/plans/{planId}")
  fun getTrainingPlanDetails(@Path(PLAN_ID) planId: String): Single<TrainingPlanDao.TrainingPlan>

  @POST("/currentplan")
  fun setCurrentTrainingPlanId(@Body body: CurrentTrainingPlanInfo): Completable

  @GET("/currentplan")
  fun getCurrentTrainingPlanId(): Single<CurrentTrainingPlanInfo>
}