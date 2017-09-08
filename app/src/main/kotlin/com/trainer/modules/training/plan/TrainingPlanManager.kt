package com.trainer.modules.training.plan

import com.trainer.d2.scope.ApplicationScope
import com.trainer.persistence.training.TrainingRepository
import javax.inject.Inject

/**
 * Created by dariusz on 29.08.17.
 */
@ApplicationScope
class TrainingPlanManager @Inject constructor(val trainingPlanApi: TrainingPlanApi,
                                              val trainingRepository: TrainingRepository) {


}