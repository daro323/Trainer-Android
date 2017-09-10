package com.trainer.ui.training

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.commons.typedviewholder.TypedViewHolderAdapter
import com.trainer.commons.typedviewholder.registerHolder
import com.trainer.extensions.invisibleView
import com.trainer.extensions.showQuickMessage
import com.trainer.extensions.start
import com.trainer.extensions.visibleView
import com.trainer.viewmodel.training.TrainingPlanItem
import com.trainer.viewmodel.training.TrainingPlanViewModel
import com.trainer.viewmodel.training.TrainingPlanViewModel.ViewStatus.*
import com.trainer.viewmodel.training.TrainingPlanViewModel.ViewStatus.ErrorType.REFRESH_PLAN_LIST_FAILURE
import com.trainer.viewmodel.training.TrainingPlanViewModel.ViewStatus.ErrorType.SELECT_PLAN_FAILURE
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.item_training_plan.view.*

class TrainingPlanListActivity : BaseActivity(R.layout.activity_list) {

  private lateinit var trainingPlanVM: TrainingPlanViewModel

  private val adapter = TypedViewHolderAdapter.Builder<Any>().apply {
    registerHolder(R.layout.item_training_plan) { model: TrainingPlanItem ->
      itemView.apply {
        ui_training_plan_container.apply {
          setOnClickListener { openTrainingPlan(model) }
          if(model.isCurrent) setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
        }
        ui_name.text = model.planName
      }
    }
  }.build()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
    trainingPlanVM = getViewModel(TrainingPlanViewModel::class.java).apply {
      observeLiveData(trainingPlansStream, onShowTrainingPlans)
      observeLiveData(viewStatusStream, onViewStatus)
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

  private val onViewStatus = { viewStatus: TrainingPlanViewModel.ViewStatus? ->
    when (viewStatus) {
      is ACTIVE -> {
        ui_plans_list.visibleView()
        ui_loading_view.invisibleView()
      }
      is ERROR -> {
        ui_plans_list.visibleView()
        ui_loading_view.invisibleView()
        onHandleError(viewStatus)
      }
      is BUSY -> {
        ui_plans_list.visibleView()
        ui_loading_view.invisibleView()
      }
      is PLAN_SELECTED -> {
        showTrainingPlan()
      }
      else -> {
      }  // Ignore
    }
    Unit
  }

  private val openTrainingPlan = { planItem: TrainingPlanItem -> trainingPlanVM.selectTrainingPlan(planItem.planId) }

  private fun onHandleError(error: ERROR) {
    when (error.type) {
      REFRESH_PLAN_LIST_FAILURE -> showRetryPopupAlert(error.messageId,
          { trainingPlanVM.refreshTrainingPlans() },
          { trainingPlanVM.dismissStatusError() })

      SELECT_PLAN_FAILURE -> showRetryPopupAlert(error.messageId,
          { trainingPlanVM.selectTrainingPlan(error.extra as String) },
          {
            trainingPlanVM.dismissStatusError()
            showTrainingPlan()
          })
    }
  }

  private fun showTrainingPlan() {
    start<TrainingDaysListActivity>()
    finish()
  }
}