package com.trainer.base

import android.app.Application
import android.content.Context
import com.trainer.d2.common.ActivityComponent
import com.trainer.d2.common.ActivityModule
import com.trainer.d2.common.AppComponent
import com.trainer.modules.init.TrainingDataInitializer
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
abstract class BaseApplication : Application() {

  @Inject lateinit var dataInitializer: TrainingDataInitializer
  lateinit var appComponent: AppComponent

  companion object {
    fun get(context: Context): BaseApplication {
      return context.applicationContext as BaseApplication
    }
  }

  override fun onCreate() {
    super.onCreate()
    initDagger2()
  }

  fun activityComponent(activity: BaseActivity): ActivityComponent {
    return appComponent.plusActivity(ActivityModule(activity))
  }

  fun appComponent(): AppComponent {
    return appComponent
  }

  private fun initDagger2() {
    appComponent = onCreateAppComponent()
    appComponent.inject(this)
  }

  protected abstract fun onCreateAppComponent(): AppComponent
}