package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.modules.training.TrainingManager
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
    Log.w("xxx", "onTrainingDayClicked not implemented yet, but clicked ${item.trainingCategory.name}")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
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
    trainingDaysSubscription = trainingManager.getAllTrainingDaysData()
        .flatMap { Observable.from(it) }
        .map { TrainingDayItem(it.category, it.totalDone) }
        .toList()
        .ioMain()
        .subscribe { typedAdapter.data = it as List<TrainingDayItem> }
  }
}