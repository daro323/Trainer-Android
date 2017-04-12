package de.neofonie.commons.kt_ext

import android.util.Log

/**
 * Created by q on 01/10/16.
 */
fun Any.logd(msg: String) = Log.d(getTag(), msg)

fun Any.logd(msg: String, throwable: Throwable) = Log.d(getTag(), msg, throwable)

fun Any.logw(msg: String) = Log.w(getTag(), msg)
fun Any.logw(msg: String, throwable: Throwable) = Log.w(getTag(), msg, throwable)

fun Any.loge(msg: String) = Log.e(getTag(), msg)
fun Any.loge(msg: String, throwable: Throwable) = Log.e(getTag(), msg, throwable)

private fun getTag(): String {
  val stack = Thread.currentThread().stackTrace
  return "(${stack[4].fileName}:${stack[4].lineNumber}) ${stack[4].methodName}()"
}