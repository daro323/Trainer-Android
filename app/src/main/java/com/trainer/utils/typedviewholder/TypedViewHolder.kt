package com.trainer.utils.typedviewholder

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.trainer.extensions.inflate

abstract class TypedViewHolder<T> : RecyclerView.ViewHolder {

  protected var context: Context

  constructor(@LayoutRes layoutRes: Int, parent: ViewGroup) : super(parent.inflate(layoutRes, parent, false)) {
    context = parent.context
  }

  constructor(view: View) : super(view) {
    context = view.context
  }

  abstract fun bind(dataItem: T)
}
