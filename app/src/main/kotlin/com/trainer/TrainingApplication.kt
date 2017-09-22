package com.trainer

import android.app.Activity
import com.trainer.base.BaseApplication
import com.trainer.d2.common.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
class TrainingApplication : BaseApplication(), HasActivityInjector {

  @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

  override fun activityInjector(): AndroidInjector<Activity> = activityInjector

  override fun onCreate() {
    super.onCreate()
    DaggerAppComponent.builder().create(this).inject(this)
  }
}