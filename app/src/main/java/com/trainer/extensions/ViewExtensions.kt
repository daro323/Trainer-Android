package com.trainer.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by dariusz on 05/01/17.
 */
fun View.inflate(@LayoutRes resource: Int, root: ViewGroup?, attachToRoot: Boolean) = LayoutInflater.from(context).inflate(resource, root, attachToRoot)