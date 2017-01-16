package com.trainer.d2.common

import com.trainer.base.BaseApplication
import com.trainer.service.RestService

interface AppComponent {

  fun plusActivity(activityModule: ActivityModule): ActivityComponent

  fun inject(app: BaseApplication)

  fun inject(service: RestService)
}