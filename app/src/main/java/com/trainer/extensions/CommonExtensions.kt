package com.trainer.extensions

import android.content.SharedPreferences

/**
 * Created by dariusz on 05/01/17.
 */

fun SharedPreferences.saveString(key: String, value: String) {
  edit()
      .putString(key, value)
      .apply()
}