package com.trainer.extensions

import android.app.Activity
import android.content.Intent

/**
 * Created by dariusz on 06/01/17.
 */
inline fun <reified T : Activity> Activity.startActivity() = startActivity(Intent(this, T::class.java))