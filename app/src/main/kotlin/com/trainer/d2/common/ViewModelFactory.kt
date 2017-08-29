package com.trainer.d2.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.trainer.TrainingApplication

/**
 * Created by dariusz on 29.08.17.
 */
class ViewModelFactory(private val application: TrainingApplication) : ViewModelProvider.NewInstanceFactory() {

  override fun <T : ViewModel> create(modelClass: Class<T>) = super.create(modelClass).apply {
    if (this is AppComponent.Injectable) inject(application.appComponent)
  }
}