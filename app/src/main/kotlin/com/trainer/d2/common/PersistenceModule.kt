package com.trainer.d2.common

import android.app.Application
import android.arch.persistence.room.Room
import com.trainer.persistence.training.TrainingDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by dariusz on 29.08.17.
 */
@Module
class PersistenceModule {

  companion object {
    private val TRAINING_DATABASE_NAME = "training_db"
  }

  @Provides
  @Singleton
  fun provideTrainingDatabase(context: Application) = Room.databaseBuilder(context, TrainingDatabase::class.java, TRAINING_DATABASE_NAME).build()

  @Provides
  @Singleton
  fun provideTrainingPlanDao(trainingDatabase: TrainingDatabase) = trainingDatabase.trainingPlanDao()
}