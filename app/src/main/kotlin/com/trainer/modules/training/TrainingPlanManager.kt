package com.trainer.modules.training

import android.arch.lifecycle.Transformations
import com.trainer.d2.scope.ApplicationScope
import com.trainer.persistence.TrainingPlanEntity
import javax.inject.Inject

/**
 * Created by dariusz on 29.08.17.
 */
@ApplicationScope
class TrainingPlanManager @Inject constructor(val trainingRepository: TrainingRepository) {

  fun getAllTrainingPlans() = Transformations.map(trainingRepository.getAllTrainingPlans(), {
    // TODO provide transformation
    input: List<TrainingPlanEntity>? ->  emptyList<TrainingPlan>()
  })
}