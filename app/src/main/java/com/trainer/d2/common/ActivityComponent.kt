package com.trainer.d2.common

import com.trainer.base.BaseActivity
import com.trainer.d2.scope.ActivityScope
import com.trainer.ui.*
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
@ActivityScope
interface ActivityComponent {

  fun inject(activity: BaseActivity)
  fun inject(activity: TrainingDaysListActivity)
  fun inject(activity: WorkoutListActivity)
  fun inject(activity: SerieActivity)
  fun inject(activity: RestActivity)
  fun inject(setFragment: SetFragment)
}
