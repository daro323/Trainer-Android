package com.trainer.base

import android.app.Application
import com.trainer.d2.common.AppComponent

/**
 * Created by dariusz on 05/01/17.
 */
class BaseApplication : Application() {
  lateinit var appComponent: AppComponent
}