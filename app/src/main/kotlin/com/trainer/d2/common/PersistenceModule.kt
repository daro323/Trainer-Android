package com.trainer.d2.common

import android.arch.persistence.room.Room
import android.content.Context
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.persistence.TrainingDatabase
import dagger.Module
import dagger.Provides

/**
 * Created by dariusz on 29.08.17.
 */
@Module
class PersistenceModule {

  companion object {
    private final val TRAINING_DATABASE_NAME = "training_db"
  }

  @Provides
  @ApplicationScope
  fun provideTrainingDatabase(@ForApplication context: Context) = Room.databaseBuilder(context, TrainingDatabase::class.java, TRAINING_DATABASE_NAME)

  @Provides
  @ApplicationScope
  fun provideTrainingPlanDao(trainingDatabase: TrainingDatabase) = trainingDatabase.trainingPlanDao()
}