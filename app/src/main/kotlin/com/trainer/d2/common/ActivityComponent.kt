package com.trainer.d2.common

import com.trainer.base.BaseActivity
import com.trainer.d2.scope.ActivityScope
import com.trainer.ui.training.SerieActivity
import com.trainer.ui.training.TrainingDaysListActivity
import com.trainer.ui.training.TrainingPlanListActivity
import com.trainer.ui.training.WorkoutListActivity
import com.trainer.ui.training.rest.RestActivity
import com.trainer.ui.training.stretch.StretchActivity
import com.trainer.ui.training.stretch.StretchFragment
import com.trainer.ui.training.types.cyclic.CycleFragment
import com.trainer.ui.training.types.standard.SetFragment
import com.trainer.ui.training.types.standard.SuperSetFragment
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
  fun inject(activity: TrainingPlanListActivity)

  fun inject(fragment: SetFragment)
  fun inject(fragment: StretchFragment)
  fun inject(fragment: SuperSetFragment)
  fun inject(fragment: CycleFragment)
}
