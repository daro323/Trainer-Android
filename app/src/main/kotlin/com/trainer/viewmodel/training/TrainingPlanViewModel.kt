package com.trainer.viewmodel.training

import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.support.annotation.Keep
import com.trainer.d2.common.AppComponent
import com.trainer.modules.training.workout.TrainingPlan
import com.trainer.modules.training.plan.TrainingPlanManager
import javax.inject.Inject

/**
 * Created by dariusz on 29.08.17.
 */
class TrainingPlanViewModel : ViewModel(), AppComponent.Injectable {

  @Inject lateinit var trainingPlanManager: TrainingPlanManager

  override fun inject(component: AppComponent) {
    component.inject(this)
  }

  fun getTrainingPlans() = Transformations.map(trainingPlanManager.getAllTrainingPlans(), { plans: List<TrainingPlan> ->
    plans.map { (name) -> TrainingPlanItem(name) }
  })
}

@Keep
data class TrainingPlanItem(val planName: String)