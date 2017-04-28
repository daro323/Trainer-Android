package com.trainer.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by dariusz on 05/01/17.
 */
fun View.inflate(@LayoutRes resource: Int, root: ViewGroup?, attachToRoot: Boolean) = LayoutInflater.from(context).inflate(resource, root, attachToRoot)

fun View?.goneIf(condition: Boolean) = this?.apply { visibility = if (condition) View.GONE else View.VISIBLE }

fun View?.invisibleIf(condition: Boolean) = this?.apply { visibility = if (condition) View.INVISIBLE else View.VISIBLE }

fun View?.goneView() = this?.apply { visibility = View.GONE }
fun View?.invisibleView() = this?.apply { visibility = View.INVISIBLE }
fun View?.visibleView() = this?.apply { visibility = View.VISIBLE }

fun TextView?.goneOrText(string: String?) = this?.apply {
  goneIf(string == null)
  string?.let {
    text = it
  }
}