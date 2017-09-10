package com.trainer.d2.common

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.trainer.BuildConfig
import com.trainer.commons.SerieDeserializer
import com.trainer.commons.typeadapters.jackson.ThreeTenAbpModule
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.plan.TrainingPlanApi
import com.trainer.modules.training.workout.Serie
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by dariusz on 30.08.17.
 */
@Module
class NetworkModule {

  companion object {
    fun createJackson() = ObjectMapper().apply {
      registerKotlinModule()
      registerModule(ThreeTenAbpModule())
      registerModule(SimpleModule().apply { addDeserializer(Serie::class.java, SerieDeserializer()) })
      setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
  }

  @Provides
  @ApplicationScope
  fun provideJackson() = createJackson()

  @Provides
  @ApplicationScope
  fun provideOkHttpClient() = OkHttpClient.Builder().apply {
    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
  }.build()

  @Provides
  @ApplicationScope
  fun provideRetrofit(okHttpClient: OkHttpClient, jackson: ObjectMapper) = Retrofit.Builder().apply {
    baseUrl(BuildConfig.SERVER_ENDPOINT)
    client(okHttpClient)
    addConverterFactory(JacksonConverterFactory.create(jackson))
    addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  }.build()

  @Provides @ApplicationScope fun provideTrainingPlanApi(retrofit: Retrofit) = retrofit.create(TrainingPlanApi::class.java)
}