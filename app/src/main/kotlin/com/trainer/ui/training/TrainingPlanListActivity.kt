package com.trainer.ui.training

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.commons.typedviewholder.TypedViewHolderAdapter
import com.trainer.commons.typedviewholder.registerHolder
import com.trainer.extensions.invisibleView
import com.trainer.extensions.showQuickMessage
import com.trainer.extensions.visibleView
import com.trainer.viewmodel.training.TrainingPlanItem
import com.trainer.viewmodel.training.TrainingPlanViewModel
import com.trainer.viewmodel.training.TrainingPlanViewModel.ViewStatus.*
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.item_training_plan.view.*

class TrainingPlanListActivity : BaseActivity(R.layout.activity_list) {

  private lateinit var trainingPlanVM: TrainingPlanViewModel

  private val adapter = TypedViewHolderAdapter.Builder<Any>().apply {
    registerHolder(R.layout.item_training_plan) { model: TrainingPlanItem ->
      itemView.apply {
        ui_training_plan_container.setOnClickListener { openTrainingPlan(model) }
        ui_name.text = model.planName
      }
    }
  }.build()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
    trainingPlanVM = getViewModel(TrainingPlanViewModel::class.java).apply {
      observeLiveData(getTrainingPlansStream(), onShowTrainingPlans)
      observeLiveData(getLoadingStatusStream(), onLoadingStatus)
      refreshTrainingPlans()
    }
    title = getString(R.string.select_training_plan)
  }

  private val onShowTrainingPlans = { plans: List<TrainingPlanItem>? ->
    plans?.let {
      ui_plans_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
      ui_plans_list.adapter = adapter
      adapter.data = plans
    } ?: showQuickMessage(R.string.no_training_plans)
  }

  private val onLoadingStatus = { viewStatus: TrainingPlanViewModel.ViewStatus? ->
    when (viewStatus) {
      is ACTIVE -> {
        ui_plans_list.visibleView()
        ui_loading_view.invisibleView()
      }
      is ERROR -> {
        ui_plans_list.visibleView()
        ui_loading_view.invisibleView()
        showRetryPopupAlert(viewStatus.messageId,
            { trainingPlanVM.refreshTrainingPlans() },
            { trainingPlanVM.dismissStatusError() })
      }
      is BUSY -> {
        ui_plans_list.visibleView()
        ui_loading_view.invisibleView()
      }
      else -> {
      }  // Ignore
    }
    Unit
  }

  private val openTrainingPlan = { planItem: TrainingPlanItem ->
    // TODO: Set training plan as current and open it
  }
}