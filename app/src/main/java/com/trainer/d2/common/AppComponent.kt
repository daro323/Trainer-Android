package com.trainer.d2.common

import com.trainer.base.BaseApplication

interface AppComponent {

  fun plusActivity(activityModule: ActivityModule): ActivityComponent

  fun inject(app: BaseApplication)
}