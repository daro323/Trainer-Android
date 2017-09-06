package com.trainer.modules.training.plan

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.workout.TrainingPlan
import com.trainer.persistence.TrainingPlanDao
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by dariusz on 29.08.17.
 */
@ApplicationScope
class TrainingPlanManager @Inject constructor(val trainingPlanApi: TrainingPlanApi,
                                              val trainingPlanDao: TrainingPlanDao) {

  fun getTrainingPlans(): Single<List<TrainingPlan>> {
    return trainingPlanApi.getTrainingPlans()
        .map { it.plans }
        .flatMap {
          Single.fromCallable { it.apply { trainingPlanDao.addPlans(it) } }
        }
  }

  fun getTrainingPlansLD() = trainingPlanDao.listAllPlansLD()
}