package com.trainer.base

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.trainer.BuildConfig
import com.trainer.commons.Lg

/**
 * Created by dariusz on 05/01/17.
 */
abstract class BaseApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    Lg.configure(BuildConfig.DEBUG, BuildConfig.DEBUG, 0)
    AndroidThreeTen.init(this)
  }
}