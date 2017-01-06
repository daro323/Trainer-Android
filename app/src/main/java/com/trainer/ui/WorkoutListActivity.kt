package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.extra
import com.trainer.modules.training.Series
import com.trainer.modules.training.TrainingCategory
import com.trainer.modules.training.TrainingManager
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolderAdapter
import javax.inject.Inject

/**
 * Displays workout plan for Given Training Category
 */
class WorkoutListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var trainingManager: TrainingManager
  val trainingCategory: TrainingCategory by extra(EXTRA_TRAINING_CATEGORY)
  private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
  private val typedAdapter: TypedViewHolderAdapter<Any> by lazy {
    TypedViewHolderAdapter.Builder<Any>()
        .addFactory(TrainingDayItemHolder.factory{  })
        .build()
  }

  companion object {
    val EXTRA_TRAINING_CATEGORY = "EXTRA_TRAINING_CATEGORY"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    title = String.format(getString(R.string.workout), trainingCategory)
    trainingManager.startWorkout(trainingCategory)
    trainingManager.getCurrentWorkout()?.getWorkoutList()
  }

  override fun onBackPressed() {
    if(trainingManager.isWorkoutStarted()) showAlert()
    else super.onBackPressed()
  }

  private fun showWorkoutList(list: List<Series>) {
    // TODO
  }

  private fun showAlert() {
    // TODO
  }
}
