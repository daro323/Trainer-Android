package com.trainer.d2.common

import com.trainer.base.BaseActivity
import com.trainer.d2.scope.ActivityScope
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
@ActivityScope
interface ActivityComponent {

  fun inject(baseActivity: BaseActivity)
}
