package com.trainer.d2.common

import com.trainer.d2.qualifier.DbName
import com.trainer.d2.qualifier.DbVersion
import com.trainer.d2.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

  @Provides
  @ApplicationScope
  @DbName
  fun provideDbName(): String {
    return "local.db"
  }

  @Provides
  @ApplicationScope
  @DbVersion
  fun provideDbVersion(): Int {
    return 1
  }

  // TODO: Provide DB
}
