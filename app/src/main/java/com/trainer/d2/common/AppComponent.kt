package com.trainer.d2.common

import com.trainer.base.BaseApplication
import com.trainer.modules.rest.RestService

interface AppComponent {

  fun plusActivity(activityModule: ActivityModule): ActivityComponent

  fun inject(app: BaseApplication)

  fun inject(service: RestService)
}