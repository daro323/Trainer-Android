package com.trainer.d2.common

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.trainer.d2.scope.PerActivity
import dagger.Binds
import dagger.Module

@Module
abstract class ActivityModule {

  @Binds
  @PerActivity    // Providing Application, Activity, Fragment, Service, etc does not require scoped annotations since they are the components being
  internal abstract fun activityContext(activity: AppCompatActivity): Context
}
