package com.trainer.viewmodel.training

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.annotation.Keep
import android.support.annotation.StringRes
import com.trainer.R
import com.trainer.base.BaseViewModel
import com.trainer.d2.common.AppComponent
import com.trainer.extensions.ioMain
import com.trainer.persistence.training.TrainingRepository
import javax.inject.Inject

/**
 * Created by dariusz on 29.08.17.
 */
class TrainingPlanViewModel : BaseViewModel() {

  @Inject lateinit var trainingRepo: TrainingRepository

  val viewStatusStream = MutableLiveData<ViewStatus>()
  val trainingPlansStream by lazy {
    Transformations.switchMap(currentTrainingPlanIdStream, { currentPlanId ->
      Transformations.map(trainingRepo.trainingPlanDao.getAllPlansStream(), { input -> input?.map { TrainingPlanItem(it.id, it.name, currentPlanId == it.id) } })
    })
  }
  private val currentTrainingPlanIdStream = MutableLiveData<String?>()

  override fun inject(component: AppComponent) {
    component.inject(this)
  }

  fun refreshTrainingPlans() {
    disposables.add(
        trainingRepo.getTrainingPlans()
            .flatMap {
              trainingRepo.getCurrentTrainingPlanId()
            }
            .ioMain()
            .doOnSubscribe { viewStatusStream.value = ViewStatus.BUSY }
            .subscribe(
                {
                  currentTrainingPlanIdStream.value = it
                  viewStatusStream.value = ViewStatus.ACTIVE
                },
                { viewStatusStream.value = ViewStatus.ERROR(ViewStatus.ErrorType.REFRESH_PLAN_LIST_FAILURE, R.string.failure_refresh_training_plans) }))
  }

  fun selectTrainingPlan(planId: String) {
    disposables.add(trainingRepo.setCurrentTrainingPlanId(planId)
        .ioMain()
        .doOnSubscribe { viewStatusStream.value = ViewStatus.BUSY }
        .subscribe(
            { viewStatusStream.value = ViewStatus.PLAN_SELECTED(planId) },
            { viewStatusStream.value = ViewStatus.ERROR(ViewStatus.ErrorType.SELECT_PLAN_FAILURE, R.string.failure_sync_on_select_training_plan, planId) }))
  }

  fun dismissStatusError() {
    require(viewStatusStream.value is ViewStatus.ERROR) { "Attempt to dismissStatusError but current is not of type ERROR!" }
    viewStatusStream.value = ViewStatus.ACTIVE
  }

  @Keep
  sealed class ViewStatus {
    object ACTIVE : ViewStatus()
    object BUSY : ViewStatus()
    data class PLAN_SELECTED(val planId: String) : ViewStatus()
    data class ERROR(val type: ErrorType,
                     @StringRes val messageId: Int,
                     val extra: Any? = null) : ViewStatus()

    enum class ErrorType {
      REFRESH_PLAN_LIST_FAILURE,
      SELECT_PLAN_FAILURE
    }
  }
}

@Keep
data class TrainingPlanItem(val planId: String,
                            val planName: String,
                            val isCurrent: Boolean,
                            val isAlreadyUsed: Boolean = false)
