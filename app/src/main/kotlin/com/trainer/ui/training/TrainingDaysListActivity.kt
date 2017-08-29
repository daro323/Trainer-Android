package com.trainer.ui.training

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.commons.typedviewholder.TypedViewHolderAdapter
import com.trainer.commons.typedviewholder.registerHolder
import com.trainer.modules.training.WorkoutManager
import com.trainer.extensions.ioMain
import com.trainer.extensions.start
import com.trainer.modules.export.ExportManager
import com.trainer.ui.training.types.model.TrainingDayItem
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.item_training_day.view.*
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
class TrainingDaysListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var workoutManager: WorkoutManager
  @Inject lateinit var exportManager: ExportManager

  private val adapter = TypedViewHolderAdapter.Builder<Any>().apply {
    registerHolder(R.layout.item_training_day) { model: TrainingDayItem ->
      itemView.apply {
        training_day_item_container.setOnClickListener {
          workoutManager.startWorkout(model.trainingCategory)
          start<WorkoutListActivity>()
        }
        nameText.text = model.trainingCategory
        countText.text = model.count.toString()
        if (model.daysAgo > 0) daysAgoText.text = model.daysAgo.run { String.format(context.resources.getQuantityString(R.plurals.days_ago_plurals, this), this) }
      }
    }
  }.build()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    if (workoutManager.continueWorkout()) {
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
                  { showConfirmPopupAlert(getString(R.string.export_successful) + " ${exportManager.getTrainingStorageDir()}") },
                  { error -> showConfirmPopupAlert("${getString(R.string.export_failure)}: '${error.message}'") })
        }
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun buildUi() {
    title = workoutManager.getTrainingPlan().name
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    recyclerView.adapter = adapter

    workoutManager.getTrainingPlan().trainingDays
        .flatMap { listOf(TrainingDayItem(it.category, it.getTotalDone(), it.trainedDaysAgo())) }
        .run { adapter.data = this }
  }
}