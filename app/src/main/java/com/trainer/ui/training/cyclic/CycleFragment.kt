package com.trainer.ui.training.cyclic

import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.business.WorkoutPresenter
import com.trainer.core.training.model.WorkoutEvent
import com.trainer.core.training.model.WorkoutEvent.*
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.ioMain
import com.trainer.modules.training.types.cyclic.Cycle
import com.trainer.modules.training.types.cyclic.CyclicPresenterHelper
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import kotlinx.android.synthetic.main.fragment_cycle.*
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class CycleFragment : BaseFragment(R.layout.fragment_cycle) {
  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout presenter not set!") }  // can call this only after component.inject()!
  private val presenterHelper: CyclicPresenterHelper by lazy { presenter.getHelper() as CyclicPresenterHelper }  // can call this only after component.inject()!

  private lateinit var cycle: Cycle
  private lateinit var cycleViewModel: CycleViewModel
  private val viewModelChengesProcessor = BehaviorProcessor.create<CycleViewModel>()
  private var workoutEventsSubscription = Disposables.disposed()

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    createUi(presenterHelper.getSerie() as Cycle)
    subscribeForWorkoutEvents()
  }

  override fun onStop() {
    workoutEventsSubscription.dispose()
    super.onStop()
  }

  private fun subscribeForWorkoutEvents() {
    workoutEventsSubscription.dispose()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .ioMain()
        .subscribe { handleWorkoutEvent(it) }
  }

  private fun createUi(cycle: Cycle) {
    this.cycle = cycle
    viewModelChengesProcessor.toObservable().apply {
      header.bindViewModel(this)
      body.bindViewModel(this)
      footer.bindViewModel(this)
    }
  }

  private fun handleWorkoutEvent(event: WorkoutEvent) {
    when (event) {
      REST -> {}
      PREPARE -> {}
      DO_NEXT -> {}
    }
  }
}