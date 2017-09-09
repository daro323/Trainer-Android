package com.trainer.modules.training.plan

import android.support.annotation.Keep
import com.trainer.persistence.training.TrainingPlanDao

/**
 * Created by dariusz on 09.09.17.
 */
@Keep
data class TrainingPlanListResponse(val plans: List<TrainingPlanDao.TrainingPlan>)

@Keep
data class TrainingDayResponse(val day: TrainingPlanDao.TrainingDay)

@Keep
data class CurrentTrainingPlanInfo(val currentPlanId: String)