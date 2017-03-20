package com.trainer.ui.training.cyclic

import android.os.Bundle
import android.view.View
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.business.WorkoutPresenter
import com.trainer.core.training.model.ProgressStatus
import com.trainer.core.training.model.WorkoutEvent
import com.trainer.core.training.model.WorkoutEvent.*
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.ioMain
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

  private val cycleViewModel: CycleViewModel = CycleViewModel.createNew()
  private val viewModelChengesProcessor = BehaviorProcessor.create<CycleViewModel>()
  private var workoutEventsSubscription = Disposables.disposed()

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModelChengesProcessor.toObservable().apply {
      header.bindViewModel(this)
      body.bindViewModel(this)
      footer.bindViewModel(this)
    }
    initViewModel()
  }

  override fun onStart() {
    super.onStart()
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

  private fun initViewModel() {
    val cycle = presenterHelper.getSerie()
    val currentRoutine = presenterHelper.getCurrentRoutine()

    // State
    when (cycle.status()) {
      ProgressStatus.NEW -> cycleViewModel.state = CycleState.NEW
      ProgressStatus.COMPLETE -> cycleViewModel.state = CycleState.NEW
      else -> {} // Ignore
    }

    // Header
    cycleViewModel.headerViewModel.apply {
      exerciseName = currentRoutine.exercise.name
      cycleCount = cycle.cyclesCount
      lastCycleCount = cycle.lastCyclesCount
    }

    viewModelChengesProcessor.onNext(cycleViewModel)
  }

  private fun handleWorkoutEvent(event: WorkoutEvent) {
    when (event) {
      REST -> {
      }
      PREPARE -> {
      }
      DO_NEXT -> {
      }
    }
  }
}