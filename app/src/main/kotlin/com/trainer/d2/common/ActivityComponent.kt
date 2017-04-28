package com.trainer.d2.common

import com.trainer.base.BaseActivity
import com.trainer.d2.scope.ActivityScope
import com.trainer.ui.training.*
import com.trainer.ui.training.cyclic.CycleFragment
import com.trainer.ui.training.standard.SetFragment
import com.trainer.ui.training.standard.SuperSetFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
@ActivityScope
interface ActivityComponent {

  fun inject(activity: BaseActivity)

  fun inject(activity: TrainingDaysListActivity)
  fun inject(activity: WorkoutListActivity)
  fun inject(activity: SerieActivity)
  fun inject(activity: RestActivity)
  fun inject(activity: StretchActivity)

  fun inject(fragment: SetFragment)
  fun inject(fragment: StretchFragment)
  fun inject(fragment: SuperSetFragment)
  fun inject(fragment: CycleFragment)
}
