package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.modules.training.Training
import com.trainer.modules.training.TrainingManager
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolder
import com.trainer.utils.typedviewholder.TypedViewHolderAdapter
import com.trainer.utils.typedviewholder.TypedViewHolderFactory
import rx.Observable
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
class TrainingDaysListActivity : BaseActivity(R.layout.activity_training_days_list) {

  @Inject lateinit var trainingManager: TrainingManager

  private var trainingDaysSubscription = Subscriptions.unsubscribed()
  private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
  private val typedAdapter: TypedViewHolderAdapter<Any> by lazy {
    TypedViewHolderAdapter.Builder<Any>()
        .addFactory(TrainingDayItemHolder.factory(onTrainingDayClicked))
        .build()
  }

  private val onTrainingDayClicked = { item: TrainingDayItem ->
    Log.w("xxx", "onTrainingDayClicked not implemented yet, but clicked ${item.training.name}")
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

  private fun subscribeForTrainingDays() {
    trainingDaysSubscription.unsubscribe()
    trainingDaysSubscription = trainingManager.getAllTrainingDaysData()
        .flatMap { Observable.from(it) }
        .map { TrainingDayItem(it.name, it.totalDone) }
        .toList()
        .ioMain()
        .subscribe { typedAdapter.data = it as List<TrainingDayItem> }
  }
  private data class TrainingDayItem(val training: Training,
                                     val count: Int)

  private class TrainingDayItemHolder(parent: ViewGroup,
                                      private val onClick: (TrainingDayItem) -> Any) : TypedViewHolder<TrainingDayItem>(R.layout.training_day_item, parent) {
    companion object {
      fun factory(listener: (TrainingDayItem) -> Any) = object : TypedViewHolderFactory<TrainingDayItem>(TrainingDayItem::class.java) {
        override fun build(parent: ViewGroup): TypedViewHolder<TrainingDayItem> {
          return TrainingDayItemHolder(parent, listener)
        }
      }
    }

    lateinit var model: TrainingDayItem;
    private val nameText: TextView by bindView(R.id.name)
    private val countText: TextView by bindView(R.id.count)

    init {
      nameText.setOnClickListener { onClick(model) }
    }

    override fun bind(dataItem: TrainingDayItem) {
      model = dataItem
      nameText.text = model.training.name
      countText.text = model.count.toString()
    }
  }
}