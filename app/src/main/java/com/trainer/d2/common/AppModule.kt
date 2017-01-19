package com.trainer.d2.common

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Vibrator
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.trainer.base.BaseApplication
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: BaseApplication) {

  companion object {
    private const val PREFS_NAME = "weff43r2f34f23ff-wef3"
  }

  @Provides @ApplicationScope
  fun provideApplication(): Application {
    return app
  }

  @Provides @ApplicationScope @ForApplication
  fun provideApplicationContext(): Context {
    return app.applicationContext
  }

  @Provides @ApplicationScope
  fun provideSharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }

  @Provides @ApplicationScope
  fun provideVibrator(application: Application): Vibrator {
    return application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  }

  @Provides @ApplicationScope
  fun provideNotificationManager(application: Application): NotificationManager {
    return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }

  @Provides
  @ApplicationScope
  internal fun provideJackson() = ObjectMapper().apply {
    registerKotlinModule()
    setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  }
}
