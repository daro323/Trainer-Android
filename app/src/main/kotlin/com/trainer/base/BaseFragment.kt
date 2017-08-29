package com.trainer.base

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.trainer.d2.common.ActivityComponent

/**
 * Created by dariusz on 09/01/17.
 */
abstract class BaseFragment(@LayoutRes private val layoutRes: Int = -1) : Fragment(), LifecycleRegistryOwner {

  private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = when (layoutRes) {
    -1 -> null
    else -> inflater.inflate(layoutRes, container, false)
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    inject(getComponent(context))
  }

  override fun getLifecycle() = lifecycleRegistry

  internal fun getComponent(context: Context): ActivityComponent {
    if (context !is BaseActivity) {
      throw IllegalArgumentException("This view should be attached to Activity extending BaseActivity. Please override getComponent() to provide different source of ActivityComponent to which this View should inject.")
    }
    return context.component
  }

  protected abstract fun inject(component: ActivityComponent)
}

interface OnBackSupportingFragment {
  fun onBackPressed(): Boolean
}