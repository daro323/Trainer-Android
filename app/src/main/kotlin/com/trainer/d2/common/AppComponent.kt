package com.trainer.d2.common

import com.trainer.base.BaseApplication
import com.trainer.modules.countdown.CountDownService
import com.trainer.viewmodel.training.TrainingPlanViewModel

interface AppComponent {

  fun plusActivity(activityModule: ActivityModule): ActivityComponent

  fun inject(app: BaseApplication)

  fun inject(service: CountDownService)

  fun inject(viewModel: TrainingPlanViewModel)

  interface Injectable {
    fun inject(component: AppComponent)
  }
}