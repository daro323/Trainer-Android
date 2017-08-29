package com.trainer.ui.training

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.commons.typedviewholder.TypedViewHolderAdapter
import com.trainer.commons.typedviewholder.registerHolder
import com.trainer.extensions.showQuickMessage
import com.trainer.viewmodel.training.TrainingPlanItem
import com.trainer.viewmodel.training.TrainingPlanViewModel
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
      getTrainingPlans().observe(this@TrainingPlanListActivity, Observer(showTrainingPlans))
    }
  }

  private val showTrainingPlans = { plans: List<TrainingPlanItem>? ->
    plans?.let { recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
      recyclerView.adapter = adapter
      adapter.data = plans } ?: showQuickMessage(R.string.no_training_plans)
  }

  private val openTrainingPlan = { planItem: TrainingPlanItem ->
    // TODO: Set training plan as current and open it
  }
}