package com.trainer.ui.training.model

import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.trainer.R
import com.trainer.commons.StyleUtils.Companion.getColorRes
import com.trainer.commons.bindView
import com.trainer.commons.typedviewholder.TypedViewHolder
import com.trainer.commons.typedviewholder.TypedViewHolderFactory
import com.trainer.modules.training.ProgressStatus
import com.trainer.modules.training.TrainingCategory
import eu.inmite.android.lib.validations.form.annotations.NotEmpty

/**
 * Created by dariusz on 06/01/17.
 */
// TODO: Remove this pattern
data class TrainingDayItem(val trainingCategory: TrainingCategory,
                           val count: Int,
                           val daysAgo: Int)

data class SetItem(@DrawableRes val imageRes: Int,
                   val name: String,
                   val status: ProgressStatus)

data class SuperSetItem(@DrawableRes val imageResList: List<Int>,
                        val namesList: List<String>,
                        val status: ProgressStatus)

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
    container.setBackgroundColor(ContextCompat.getColor(context, getColorRes(dataItem.status)))
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

class SetItemHolder(parent: ViewGroup,
                    private val onClick: (SetItem) -> Any) : TypedViewHolder<SetItem>(R.layout.set_item, parent) {
  companion object {
    fun factory(listener: (SetItem) -> Any) = object : TypedViewHolderFactory<SetItem>(SetItem::class.java) {
      override fun build(parent: ViewGroup): TypedViewHolder<SetItem> {
        return SetItemHolder(parent, listener)
      }
    }
  }

  lateinit var model: SetItem
  private val container: ViewGroup by bindView(R.id.content_container)
  private val imageView: ImageView by bindView(R.id.image)
  private val nameText: TextView by bindView(R.id.name)

  init {
    container.setOnClickListener { onClick(model) }
  }

  override fun bind(dataItem: SetItem) {
    model = dataItem
    nameText.text = model.name
    imageView.setImageResource(model.imageRes)
    container.setBackgroundColor(ContextCompat.getColor(context, getColorRes(dataItem.status)))
  }
}

class SetFragmentFieldValidator constructor(
    @NotEmpty(messageId = R.string.empty_field)
    val weightInput: EditText,
    @NotEmpty(messageId = R.string.empty_field)
    val repInput: EditText
)