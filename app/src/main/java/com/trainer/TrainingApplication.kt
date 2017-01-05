package com.trainer

import com.trainer.base.BaseApplication
import com.trainer.d2.common.AppComponent
import com.trainer.d2.common.AppModule
import com.trainer.d2.common.TrainingModule
import com.trainer.d2.scope.ApplicationScope
import dagger.Component

/**
 * Created by dariusz on 05/01/17.
 */
class TrainingApplication : BaseApplication() {

  override fun onCreateAppComponent(): AppComponent? {
    return DaggerTrainingApplication_TrainingAppComponent.builder().appModule(AppModule(this)).build()
  }

  @ApplicationScope
  @Component(modules = arrayOf(AppModule::class, TrainingModule::class))
  interface TrainingAppComponent : AppComponent
}