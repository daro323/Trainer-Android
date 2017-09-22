package com.trainer.modules.training.plan

import com.trainer.persistence.training.TrainingRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dariusz on 29.08.17.
 */
@Singleton
class TrainingPlanManager @Inject constructor(val trainingPlanApi: TrainingPlanApi,
                                              val trainingRepository: TrainingRepository) {


}