package com.trainer.extensions

import android.app.Activity
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by dariusz on 06/01/17.
 */
inline fun <reified T : Activity> Activity.start() = startActivity(Intent(this, T::class.java))

inline fun <reified T : Activity> Activity.start(intentSetup: Intent.() -> Unit) =
    startActivity(Intent(this, T::class.java).apply { intentSetup() })

inline fun <reified T : Activity> Activity.startForResult(requestCode: Int,
                                                          intentSetup: Intent.() -> Unit): Unit =
    startActivityForResult(Intent(this, T::class.java).apply { intentSetup() }, requestCode)

inline fun FragmentActivity.setupFragment(@IdRes containerId: Int, addToBackstack: Boolean = false, fragment: () -> Fragment): Fragment {
  var fragment = supportFragmentManager.findFragmentById(containerId)
  if (fragment == null) {
    fragment = fragment()
    supportFragmentManager.beginTransaction().apply {
      add(containerId, fragment, if (addToBackstack) fragment.javaClass.simpleName else null)
      if (addToBackstack) addToBackStack(fragment.javaClass.simpleName)
    }.commit()
  }
  return fragment
}