package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.start
import com.trainer.modules.export.ExportManager
import com.trainer.modules.training.TrainingManager
import com.trainer.ui.model.TrainingDayItem
import com.trainer.ui.model.TrainingDayItemHolder
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolderAdapter
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
class TrainingDaysListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var trainingManager: TrainingManager
  @Inject lateinit var exportManager: ExportManager

  private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
  private val typedAdapter: TypedViewHolderAdapter<Any> by lazy {
    TypedViewHolderAdapter.Builder<Any>()
        .addFactory(TrainingDayItemHolder.factory(onTrainingDayClicked))
        .build()
  }

  private val onTrainingDayClicked = { item: TrainingDayItem ->
    trainingManager.startWorkout(item.trainingCategory)
    start<WorkoutListActivity>()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    title = trainingManager.getTrainingPlanName()
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    recyclerView.adapter = typedAdapter
    showTrainingDays()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.training_plan_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.import_plan -> {
        return true
      }
      R.id.export_plan -> {
        exportManager.exportCurrentTrainingPlan()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun showTrainingDays() {
    trainingManager.getTrainingDays()
        .flatMap { listOf(TrainingDayItem(it.category, it.getTotalDone())) }
        .run { typedAdapter.data = this }
  }
}