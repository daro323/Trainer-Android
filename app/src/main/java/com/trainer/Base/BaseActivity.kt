package com.trainer.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.trainer.d2.common.ActivityComponent

/**
 * Created by dariusz on 05/01/17.
 */
abstract class BaseActivity(@LayoutRes private val layoutRes: Int = -1) : AppCompatActivity() {
  val component: ActivityComponent by lazy { (applicationContext as BaseApplication).activityComponent(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (layoutRes != -1) setContentView(layoutRes)
  }
}