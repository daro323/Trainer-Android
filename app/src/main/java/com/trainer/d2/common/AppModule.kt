package com.trainer.d2.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.trainer.base.BaseApplication
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import dagger.Module
import dagger.Provides

private const val PREFS_NAME = "weff43r2f34f23ff-wef3"

@Module
class AppModule(private val app: BaseApplication) {

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
}
