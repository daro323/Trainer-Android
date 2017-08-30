package com.trainer.modules.training.plan

import android.support.annotation.Keep
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.trainer.modules.training.workout.Repetition
import com.trainer.modules.training.workout.SerieType
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by dariusz on 30.08.17.
 */
interface TrainingPlanApi {

  @GET("api/plans")
  fun getTrainingPlans(): Call<TrainingPlanListResponse>







  @Keep
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
  @JsonSubTypes(
      JsonSubTypes.Type(value = SetResponse::class, name = "SetResponse"),
      JsonSubTypes.Type(value = SuperSetResponse::class, name = "SuperSetResponse"),
      JsonSubTypes.Type(value = CycleResponse::class, name = "CycleResponse"))
  interface SerieResponse

  @Keep
  data class TrainingPlanListResponse(val plans: List<TrainingPlanResponse>)

  @Keep
  data class TrainingPlanResponse(val id: String,
                                  val name: String,
                                  val categories: kotlin.collections.Set<String>,
                                  val trainingDays: List<TrainingDayResponse>,
                                  val totalDone: Int,
                                  val lastTrainedDate: String?)

  @Keep
  data class TrainingDayResponse(val category: String,
                                 val workout: WorkoutResponse,
                                 val totalDone: Int,
                                 val lastTrainedDate: String?)

  @Keep
  data class WorkoutResponse(val series: List<SerieResponse>)

  @Keep
  data class SetResponse(val exercise_id: String,
                         val seriesCount: Int,
                         val restTimeSec: Int,
                         val guidelines: List<String>,
                         val lastProgress: List<Repetition>,
                         val type: SerieType)

  @Keep
  data class SuperSetResponse(val name: String,
                              val seriesList: List<SetResponse>,
                              val type: SerieType)

  @Keep
  data class CycleResponse(val name: String,
                           val cycleList: List<CyclicRoutineResponse>,
                           val restTimeSec: Int,
                           val lastCyclesCount: Int,
                           val type: SerieType)

  @Keep
  data class CyclicRoutineResponse constructor(val exercise_id: String,
                                               val durationTimeSec: Int,
                                               val restTimeSec: Int)
}