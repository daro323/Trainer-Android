package com.trainer.persistence.training

import android.content.SharedPreferences
import com.fasterxml.jackson.databind.ObjectMapper
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.saveString
import com.trainer.modules.training.plan.TrainingPlanApi
import com.trainer.modules.training.workout.types.standard.StretchPlan
import com.trainer.persistence.training.TrainingPlanDao.TrainingDay
import com.trainer.persistence.training.TrainingPlanDao.TrainingPlan
import io.reactivex.Single
import javax.inject.Inject

/**
 * Responsible for providing data to the app (abstracts the source)
 *
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingRepository @Inject constructor(val trainingPlanDao: TrainingPlanDao,
                                             val trainingPlanApi: TrainingPlanApi,
                                             val sharedPrefs: SharedPreferences,
                                             val mapper: ObjectMapper) {

  companion object {
    private const val KEY_USER_DETAILS = "KEY_USER_DETAILS"
  }

  private var currentPlanId: String = ""
    get() {
      if (field.isEmpty()) {
        sharedPrefs.getString(KEY_USER_DETAILS, "")?.run { field = this }
      }
      return field
    }
    set(value) {
      sharedPrefs.saveString(KEY_USER_DETAILS, value)
      field = value
    }

  fun getTrainingPlans(): Single<TrainingPlansList> {
    return trainingPlanApi.getTrainingPlans()
        .map { TrainingPlansList(it.plans) }
        .onErrorReturn { TrainingPlansList(trainingPlanDao.getAllPlans(), true)  }
        .flatMap {
          Single.fromCallable { it.apply { trainingPlanDao.addPlans(it.plans) } }
        }
  }

  fun setCurrentTrainingPlanId(planId: String) = trainingPlanApi.setCurrentTrainingPlanId(TrainingPlanApi.CurrentTrainingPlanInfo(planId))
      .doFinally { currentPlanId = planId }

  fun getCurrentTrainingPlanId() = trainingPlanApi.getCurrentTrainingPlanId()
      .map { it.currentPlanId.apply { currentPlanId = this } }
      .onErrorReturn { currentPlanId }






  // TODO: Remove deprecated stuff
  fun getTrainingPlan() = TrainingPlan("1", "Not initialized", setOf())
  fun getStretchPlan() = StretchPlan(emptyList())
  fun setNewTrainingPlan(newPlan: TrainingPlan) {}
  fun saveTrainingDay(day: TrainingDay) {}
  fun saveTrainingPlan() {}
  fun saveStretchPlan(stretchPlan: StretchPlan) {}
  fun hasStretchPlan() = false
}