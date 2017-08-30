package com.trainer.modules.training.plan

import android.arch.lifecycle.Transformations
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.workout.TrainingPlan
import com.trainer.persistence.TrainingPlanEntity
import de.neofonie.commons.kt_ext.logd
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.await
import javax.inject.Inject

/**
 * Created by dariusz on 29.08.17.
 */
@ApplicationScope
class TrainingPlanManager @Inject constructor(val trainingPlanApi: TrainingPlanApi,
                                              val trainingRepository: TrainingRepository) {

  fun getAllTrainingPlans() = Transformations.map(trainingRepository.getAllTrainingPlans(), {
    // TODO provide transformation
    input: List<TrainingPlanEntity>? ->  emptyList<TrainingPlan>()
  })


  fun getAllTrainingPlansAsync() {
    launch(CommonPool) {
      try {
        val response = trainingPlanApi.getTrainingPlans().await()
        logd("Success, response= $response")
      } catch (e: Throwable) {
        logd("Error fetching plans, error= ${e.message}")
      }
    }
  }
}