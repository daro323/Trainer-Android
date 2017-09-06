package com.trainer.viewmodel.training

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.annotation.Keep
import android.support.annotation.StringRes
import com.trainer.R
import com.trainer.base.BaseViewModel
import com.trainer.d2.common.AppComponent
import com.trainer.extensions.ioMain
import com.trainer.modules.training.plan.TrainingPlanManager
import javax.inject.Inject

/**
 * Created by dariusz on 29.08.17.
 */
class TrainingPlanViewModel : BaseViewModel() {

  @Inject lateinit var trainingPlanManager: TrainingPlanManager

  private val viewStatus = MutableLiveData<ViewStatus>()

  override fun inject(component: AppComponent) {
    component.inject(this)
  }

  fun getTrainingPlansStream(): LiveData<List<TrainingPlanItem>> = Transformations.map(trainingPlanManager.getTrainingPlansLD(),
      { input -> input?.map { TrainingPlanItem(it.name) } })

  fun getLoadingStatusStream() = viewStatus

  fun refreshTrainingPlans() {
    disposables.add(trainingPlanManager.getTrainingPlans()
        .ioMain()
        .doOnSubscribe { viewStatus.value = ViewStatus.BUSY }
        .subscribe(
            { viewStatus.value = ViewStatus.ACTIVE },
            { error -> viewStatus.value = ViewStatus.ERROR(R.string.failure_refresh_training_plans) }))
  }

  fun dismissStatusError() {
    require(viewStatus.value is ViewStatus.ERROR) { "Attempt to dismissStatusError but current is not of type ERROR!" }
    viewStatus.value = ViewStatus.ACTIVE
  }

  sealed class ViewStatus {
    object ACTIVE : ViewStatus()
    object BUSY : ViewStatus()
    data class ERROR(@StringRes val messageId: Int) : ViewStatus()
  }
}

@Keep
data class TrainingPlanItem(val planName: String)