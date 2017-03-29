package com.trainer.commons

import android.util.Log

/**
 * Created by dariusz on 31/03/16.
 *
 * Logging wrapper for the project - creates tag (calling class name) by itself, adds line number and method name to help identify the log location.
 */
object Lg {

  private var __DEBUG = true

  private var LOGLEVEL = if (__DEBUG) Log.VERBOSE else Log.ERROR + 1
  private var NO_WRAPPING = false

  private var LOG_STACK_LEVEL = 1
  private val STACK_PREV_CALLS = 4
  private var ERROR = LOGLEVEL - 1 < Log.ERROR
  private var WARN = LOGLEVEL - 1 < Log.WARN
  private var INFO = LOGLEVEL - 1 < Log.INFO
  private var DEBUG = LOGLEVEL - 1 < Log.DEBUG
  private var VERBOSE = LOGLEVEL - 1 < Log.VERBOSE

  /**
   * Configures logging tool.

   * @param debug     - debug level for logging, by default true (show all logs). If false no logs are shown.
   * *
   * @param wrapCalls - if true logs will contain class/method/line number information, by default true.
   * *
   * @param wrapLevel - defines how many previous calls will be attached to the log, by default 1.
   */
  fun configure(debug: Boolean, wrapCalls: Boolean, wrapLevel: Int) {
    LOG_STACK_LEVEL = wrapLevel
    NO_WRAPPING = !wrapCalls
    __DEBUG = debug

    LOGLEVEL = if (__DEBUG) Log.VERBOSE else Log.ERROR + 1
    ERROR = LOGLEVEL - 1 < Log.ERROR
    WARN = LOGLEVEL - 1 < Log.WARN
    INFO = LOGLEVEL - 1 < Log.INFO
    DEBUG = LOGLEVEL - 1 < Log.DEBUG
    VERBOSE = LOGLEVEL - 1 < Log.VERBOSE
  }

  fun d(tag: String, message: String) {
    if (DEBUG) {
      Log.d(tag, wrapMessage(message))
    }
  }

  fun i(tag: String, message: String) {
    if (INFO) {
      Log.i(tag, wrapMessage(message))
    }
  }

  fun w(tag: String, message: String) {
    if (WARN) {
      Log.w(tag, wrapMessage(message))
    }
  }

  fun w(tag: String, message: String, tr: Throwable) {
    if (WARN) {
      Log.w(tag, wrapMessage(message), tr)
    }
  }

  fun w(tag: String, message: String, e: Exception) {
    if (WARN) {
      Log.w(tag, wrapMessage(message), e)
    }
  }

  fun v(tag: String, message: String) {
    if (VERBOSE) {
      Log.v(tag, wrapMessage(message))
    }
  }

  fun e(tag: String, message: String) {
    if (ERROR) {
      Log.e(tag, wrapMessage(message))
    }
  }

  fun e(tag: String, message: String, tr: Throwable) {
    if (ERROR) {
      Log.e(tag, wrapMessage(message), tr)
    }
  }

  fun e(tag: String, message: String, e: Exception) {
    if (ERROR) {
      Log.e(tag, wrapMessage(message), e)
    }
  }

  fun d(message: String) {
    if (DEBUG) {
      Log.d(tag, wrapMessage(message))
    }
  }

  fun i(message: String) {
    if (INFO) {
      Log.i(tag, wrapMessage(message))
    }
  }

  fun w(message: String) {
    if (WARN) {
      Log.w(tag, wrapMessage(message))
    }
  }

  fun w(message: String, tr: Throwable) {
    if (WARN) {
      Log.w(tag, wrapMessage(message), tr)
    }
  }

  fun w(message: String, e: Exception) {
    if (WARN) {
      Log.w(tag, wrapMessage(message), e)
    }
  }

  fun v(message: String) {
    if (VERBOSE) {
      Log.v(tag, wrapMessage(message))
    }
  }

  fun e(message: String) {
    if (ERROR) {
      Log.e(tag, wrapMessage(message))
    }
  }

  fun e(message: String, tr: Throwable) {
    if (ERROR) {
      Log.e(tag, wrapMessage(message), tr)
    }
  }

  fun e(message: String, e: Exception) {
    if (ERROR) {
      Log.e(tag, wrapMessage(message), e)
    }
  }

  private val tag: String
    get() {
      val stackTrace = Thread.currentThread().stackTrace
      if (stackTrace.size > STACK_PREV_CALLS) {
        return stackTrace[STACK_PREV_CALLS].className.replace(".*\\.".toRegex(), "")
      }

      return String()
    }

  /**
   * Wraps log message with information about method name and code line. Depending on the value of
   * BdeLog.LOG_STACK_LEVEL includes previous call traces.

   * @param message message to be wrapped
   * *
   * @return wrapped string message
   */
  private fun wrapMessage(message: String): String {
    if (NO_WRAPPING) {
      return message
    }
    var wrapperLevel = LOG_STACK_LEVEL + STACK_PREV_CALLS
    val stackTrace = Thread.currentThread().stackTrace
    val sb = StringBuilder()
    for (i in STACK_PREV_CALLS..wrapperLevel - 1) {

      if (stackTrace.size <= i) {
        break
      }
      if (stackTrace[i].lineNumber == 1) {
        wrapperLevel++
        continue
      }

      sb.run {
        append('[')
        append(stackTrace[i].fileName)
        append(":")
        append(stackTrace[i].methodName)
        append(":")
        append(stackTrace[i].lineNumber)
        append("] ")
      }
    }
    return sb.append(message).toString()
  }
}