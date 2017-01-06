package com.trainer.ui

import android.view.ViewGroup
import android.widget.TextView
import com.trainer.R
import com.trainer.modules.training.TrainingCategory
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolder
import com.trainer.utils.typedviewholder.TypedViewHolderFactory

/**
 * Created by dariusz on 06/01/17.
 */
data class TrainingDayItem(val trainingCategory: TrainingCategory,
                                   val count: Int)

class TrainingDayItemHolder(parent: ViewGroup,
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
    nameText.text = model.trainingCategory.name
    countText.text = model.count.toString()
  }
}



data class SetItem(val) //todo