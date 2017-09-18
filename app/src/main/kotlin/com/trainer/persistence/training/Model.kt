package com.trainer.persistence.training

/**
 * Created by dariusz on 18.09.17.
 */
data class TrainingPlansList(val plans: List<TrainingPlanDao.TrainingPlan>,
                             val isLocal: Boolean = false)