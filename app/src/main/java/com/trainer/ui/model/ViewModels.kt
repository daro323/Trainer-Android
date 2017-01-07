package com.trainer.ui.model

import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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

  lateinit var model: TrainingDayItem
  private val container: ViewGroup by bindView(R.id.content_container)
  private val nameText: TextView by bindView(R.id.name)
  private val countText: TextView by bindView(R.id.count)

  init {
    container.setOnClickListener { onClick(model) }
  }

  override fun bind(dataItem: TrainingDayItem) {
    model = dataItem
    nameText.text = model.trainingCategory.name
    countText.text = model.count.toString()
  }
}

data class SetItem(@DrawableRes val imageRes: Int,
                   val name: String)

data class SuperSetItem(@DrawableRes val imageResList: List<Int>,
                        val namesList: List<String>)

class SuperSetItemHolder(parent: ViewGroup,
                         private val onClick: (SuperSetItem) -> Any) : TypedViewHolder<SuperSetItem>(R.layout.super_set_container_item, parent) {

  companion object {
    fun factory(listener: (SuperSetItem) -> Any) = object : TypedViewHolderFactory<SuperSetItem>(SuperSetItem::class.java) {
      override fun build(parent: ViewGroup): TypedViewHolder<SuperSetItem> {
        return SuperSetItemHolder(parent, listener)
      }
    }
  }

  lateinit var model: SuperSetItem
  private val container: LinearLayout by bindView(R.id.super_set_container)

  init {
    container.setOnClickListener { onClick(model) }
  }

  override fun bind(dataItem: SuperSetItem) {
    model = dataItem
    require(dataItem.imageResList.size == dataItem.namesList.size) { "Super set adapter item invalid - list of images is not the same size as list of names!" }
    dataItem.imageResList.forEachIndexed { i, imageRes ->
      container.addView(createSetView(LayoutInflater.from(context), imageRes, dataItem.namesList[i], i == dataItem.imageResList.lastIndex))
    }
  }

  private fun createSetView(inflater: LayoutInflater, @DrawableRes setImageRes: Int, setName: String, isLast: Boolean): View {
    val view = inflater.inflate(if (isLast) R.layout.set_item else R.layout.super_set_item, container, false)
    val image = view.findViewById(R.id.image) as ImageView
    val name = view.findViewById(R.id.name) as TextView

    image.setImageResource(setImageRes)
    name.text = setName
    return view
  }
}