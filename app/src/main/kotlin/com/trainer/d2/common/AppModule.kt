package com.trainer.d2.common

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.PowerManager
import android.os.Vibrator
import com.trainer.TrainingApplication
import com.trainer.d2.scope.PerActivity
import com.trainer.d2.scope.PerFragment
import com.trainer.d2.scope.PerService
import com.trainer.modules.countdown.CountDownService
import com.trainer.ui.training.SerieActivity
import com.trainer.ui.training.TrainingDaysListActivity
import com.trainer.ui.training.WorkoutListActivity
import com.trainer.ui.training.rest.RestActivity
import com.trainer.ui.training.stretch.StretchActivity
import com.trainer.ui.training.stretch.StretchFragment
import com.trainer.ui.training.types.cyclic.CycleFragment
import com.trainer.ui.training.types.standard.SetFragment
import com.trainer.ui.training.types.standard.SuperSetFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(includes = arrayOf(
    AndroidInjectionModule::class,
    NetworkModule::class,
    PersistenceModule::class))
abstract class AppModule {

  @Module
  companion object {
    private const val PREFS_NAME = "weff43r2f34f23ff-wef3"

    @Provides
    @Singleton
    @JvmStatic
    fun provideSharedPreferences(application: Application) = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @JvmStatic
    fun provideVibrator(application: Application) = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @Provides
    @Singleton
    @JvmStatic
    fun providePowerManager(application: Application) = application.getSystemService(Context.POWER_SERVICE) as PowerManager

    @Provides
    @Singleton
    @JvmStatic
    fun provideNotificationManager(application: Application) = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }

  @Binds
  @Singleton
  abstract fun provideApplication(app: TrainingApplication): Application

  @PerActivity
  @ContributesAndroidInjector()
  internal abstract fun trainingDaysListActivityInjector(): TrainingDaysListActivity

  @PerActivity
  @ContributesAndroidInjector()
  internal abstract fun workoutListActivityInjector(): WorkoutListActivity

  @PerActivity
  @ContributesAndroidInjector()
  internal abstract fun serieActivityInjector(): SerieActivity

  @PerActivity
  @ContributesAndroidInjector()
  internal abstract fun stretchActivityInjector(): StretchActivity

  @PerActivity
  @ContributesAndroidInjector()
  internal abstract fun sestActivityInjector(): RestActivity



  @PerFragment
  @ContributesAndroidInjector()
  internal abstract fun setFragmentInjector(): SetFragment

  @PerFragment
  @ContributesAndroidInjector()
  internal abstract fun stretchFragmentInjector(): StretchFragment

  @PerFragment
  @ContributesAndroidInjector()
  internal abstract fun superSetFragmentInjector(): SuperSetFragment

  @PerFragment
  @ContributesAndroidInjector()
  internal abstract fun cycleFragmentInjector(): CycleFragment



  @PerService
  @ContributesAndroidInjector()
  internal abstract fun countDownServiceInjector(): CountDownService
}
