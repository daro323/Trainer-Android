package com.trainer.ui.training.cyclic

import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.business.WorkoutPresenter
import com.trainer.d2.common.ActivityComponent
import com.trainer.modules.training.types.cyclic.Cycle
import com.trainer.modules.training.types.cyclic.CyclicPresenterHelper
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CycleFragment : BaseFragment(R.layout.fragment_cycle) {
  private lateinit var cycle: Cycle
  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout presenter not set!") }  // can call this only after component.inject()!
  private val presenterHelper: CyclicPresenterHelper by lazy { presenter.getHelper() as CyclicPresenterHelper }  // can call this only after component.inject()!

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
  }
}