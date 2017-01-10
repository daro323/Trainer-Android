package com.trainer.extensions

import android.app.Activity
import android.content.Intent

/**
 * Created by dariusz on 06/01/17.
 */
inline fun <reified T : Activity> Activity.start() = startActivity(Intent(this, T::class.java))

inline fun <reified T : Activity> Activity.startForResult(requestCode: Int,
                                                          intentSetup: Intent.() -> Unit): Unit =
    startActivityForResult(Intent(this, T::class.java).apply { intentSetup() }, requestCode)