package com.trainer.d2.common

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.init.InitDataWorkoutProvider
import com.trainer.modules.training.WorkoutProvider
import dagger.Module
import dagger.Provides

/**
 * Created by dariusz on 05/01/17.
 */
@Module
class TrainingModule {

  @Provides @ApplicationScope
  fun provideWorkoutProvider(): WorkoutProvider {
    return InitDataWorkoutProvider()
  }
}