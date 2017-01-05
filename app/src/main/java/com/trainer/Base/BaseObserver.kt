package com.trainer.base

import android.util.Log
import rx.Observer

open class BaseObserver<T>(message: String? = null) : Observer<T> {

  companion object {
    private val DEFAULT_MESSAGE = "Error in [%s]"
  }

  private val errorMessage: String

  init {
    errorMessage = String.format(DEFAULT_MESSAGE, javaClass) + ": " + (message ?: "")
  }

  override fun onError(e: Throwable) {
    Log.e("BaseObserver", errorMessage, e)
  }

  override fun onNext(t: T) {
  }

  override fun onCompleted() {

  }
}