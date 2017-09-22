package com.trainer.d2.common

import com.trainer.TrainingApplication
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent : AndroidInjector<TrainingApplication> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<TrainingApplication>()
}