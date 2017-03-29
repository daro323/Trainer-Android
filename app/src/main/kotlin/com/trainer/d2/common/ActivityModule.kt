package com.trainer.d2.common

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import com.trainer.d2.qualifier.ForActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val mFragmentActivity: FragmentActivity) {

  @Provides
  @ForActivity
  fun provideActivityContext(): Context {
    return mFragmentActivity
  }

  @Provides
  @ForActivity
  fun provideLayoutInflater(activity: Activity): LayoutInflater {
    return LayoutInflater.from(activity)
  }

  @Provides
  fun provideActivity(): Activity {
    return mFragmentActivity
  }

  @Provides
  fun provideFragmentActivity(): FragmentActivity {
    return mFragmentActivity
  }
}
