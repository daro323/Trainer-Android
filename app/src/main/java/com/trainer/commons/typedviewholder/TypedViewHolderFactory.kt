package com.trainer.commons.typedviewholder

import android.view.ViewGroup

abstract class TypedViewHolderFactory<T>(val viewHolderType: Class<T>) {

  abstract fun build(parent: ViewGroup): TypedViewHolder<T>
}