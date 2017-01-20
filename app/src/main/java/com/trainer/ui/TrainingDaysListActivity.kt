package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
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
    if (trainingManager.continueTraining()) {
      start<WorkoutListActivity>()
    } else {
      buildUi()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.training_plan_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.import_plan -> {
        exportManager.getTrainingStorageDir()?.run {
          showFilePickerDialog(this) {
            exportManager.importTrainingPlan(it)
                .ioMain()
                .subscribe(
                    { showConfirmPopupAlert(R.string.import_successful) { buildUi() } },
                    { error -> showConfirmPopupAlert("${getString(R.string.import_failure)}: '${error.message}'") })
          }
        } ?: showConfirmPopupAlert(R.string.failure_create_external_storage)

        return true
      }
      R.id.export_plan -> {
        showCancelablePopupAlert(R.string.export_plan_warning) {
          exportManager.exportCurrentTrainingPlan()
              .ioMain()
              .subscribe(
                  { showConfirmPopupAlert(R.string.export_successful) },
                  { error -> showConfirmPopupAlert("${getString(R.string.export_failure)}: '${error.message}'") })
        }
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun buildUi() {
    title = trainingManager.getTrainingPlan().name
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    recyclerView.adapter = typedAdapter

    trainingManager.getTrainingPlan().trainingDays
        .flatMap { listOf(TrainingDayItem(it.category, it.getTotalDone())) }
        .run { typedAdapter.data = this }
  }
}