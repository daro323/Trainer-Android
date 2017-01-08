package com.trainer.d2.common

import com.trainer.base.BaseActivity
import com.trainer.d2.scope.ActivityScope
import com.trainer.ui.RestActivity
import com.trainer.ui.SetActivity
import com.trainer.ui.TrainingDaysListActivity
import com.trainer.ui.WorkoutListActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
@ActivityScope
interface ActivityComponent {

  fun inject(activity: BaseActivity)
  fun inject(activity: TrainingDaysListActivity)
  fun inject(activity: WorkoutListActivity)
  fun inject(activity: SetActivity)
  fun inject(activity: RestActivity)
}
