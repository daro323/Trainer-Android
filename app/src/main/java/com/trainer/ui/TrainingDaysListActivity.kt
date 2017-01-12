package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.extensions.start
import com.trainer.modules.training.TrainingManager
import com.trainer.ui.model.TrainingDayItem
import com.trainer.ui.model.TrainingDayItemHolder
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolderAdapter
import rx.Observable
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
class TrainingDaysListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var trainingManager: TrainingManager

  private var trainingDaysSubscription = Subscriptions.unsubscribed()
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
    title = getString(R.string.select_training)
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    recyclerView.adapter = typedAdapter
    subscribeForTrainingDays()
  }

  override fun onStop() {
    trainingDaysSubscription.unsubscribe()
    super.onStop()
  }

  private fun subscribeForTrainingDays() {
    trainingDaysSubscription.unsubscribe()
    trainingDaysSubscription = trainingManager.getTrainingDays()
        .flatMap { Observable.from(it) }
        .map { TrainingDayItem(it.category, it.getTotalDone()) }
        .toList()
        .ioMain()
        .subscribe { typedAdapter.data = it as List<TrainingDayItem> }
  }
}