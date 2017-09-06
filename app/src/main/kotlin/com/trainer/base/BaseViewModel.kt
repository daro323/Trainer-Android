package com.trainer.base

import android.arch.lifecycle.ViewModel
import com.trainer.d2.common.AppComponent
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by dariusz on 05.09.17.
 */
abstract class BaseViewModel : ViewModel(), AppComponent.Injectable {

  protected val disposables = CompositeDisposable()

  override fun onCleared() {
    disposables.dispose()
  }
}