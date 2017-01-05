package com.trainer.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.modules.training.Training
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolder
import com.trainer.utils.typedviewholder.TypedViewHolderAdapter
import com.trainer.utils.typedviewholder.TypedViewHolderFactory

/**
 * Created by dariusz on 05/01/17.
 */
class TrainingDaysListActivity : BaseActivity(R.layout.activity_training_days_list) {
  private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
  private val typedAdapter: TypedViewHolderAdapter<Any> by lazy {
    TypedViewHolderAdapter.Builder<Any>()
        .addFactory(TrainingDayItemHolder.factory(onTrainingDayClicked))
        .build()
  }

  private val onTrainingDayClicked = { item: TrainingDayItem ->
    Log.w("xxx", "onTrainingDayClicked not implemented yet, but clicked ${item.training.name}")
  }

  override fun onStart() {
    super.onStart()
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    recyclerView.adapter = typedAdapter
    typedAdapter.data = Training.values().map(::TrainingDayItem)
  }

  private data class TrainingDayItem(val training: Training)

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
      countText.text = 666.toString()
    }
  }
}