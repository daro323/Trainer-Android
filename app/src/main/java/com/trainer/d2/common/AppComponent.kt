package com.trainer.d2.common

import com.trainer.base.BaseApplication
import com.trainer.modules.countdown.CountDownService

interface AppComponent {

  fun plusActivity(activityModule: ActivityModule): ActivityComponent

  fun inject(app: BaseApplication)

  fun inject(service: CountDownService)
}