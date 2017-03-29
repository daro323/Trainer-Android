package com.trainer.commons.typedviewholder

import android.support.annotation.LayoutRes
import android.view.ViewGroup

inline fun <T : Any, reified TZ : T> TypedViewHolderAdapter.Builder<T>.registerHolder(crossinline createHolder: (ViewGroup) -> TypedViewHolder<TZ>)
  = addFactory(object : TypedViewHolderFactory<TZ>(TZ::class.java) {
  override fun build(parent: ViewGroup): TypedViewHolder<TZ> = createHolder(parent)
})

inline fun <T : Any, reified TZ : T> TypedViewHolderAdapter.Builder<T>.registerHolder(@LayoutRes layout: Int, crossinline binder: TypedViewHolder<TZ>.(TZ) -> Unit)
  = addFactory(object : TypedViewHolderFactory<TZ>(TZ::class.java) {
  override fun build(parent: ViewGroup): TypedViewHolder<TZ> = object : TypedViewHolder<TZ>(layout, parent) {
    override fun bind(dataItem: TZ) = binder(dataItem)
  }
})
