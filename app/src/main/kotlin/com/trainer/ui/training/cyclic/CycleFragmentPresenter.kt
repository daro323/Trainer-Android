package com.trainer.ui.training.cyclic

import com.trainer.modules.training.types.cyclic.CycleState.*
import com.trainer.modules.training.types.cyclic.CyclicRoutine
import io.reactivex.processors.BehaviorProcessor

/**
 * Created by dariusz on 12.04.17.
 */
class CycleFragmentPresenter(private val onViewEventHandler: (CycleViewEvent) -> Unit) : CycleViewCallback {

  private val viewModel: CycleViewModel = CycleViewModel.createNew()
  private val viewModelChengesProcessor = BehaviorProcessor.create<CycleViewModel>()

  override fun getViewModelChanges() = viewModelChengesProcessor.toObservable()

  override fun onViewEvent(event: CycleViewEvent) {
    onViewEventHandler(event)
  }

  fun getDisplayState() = viewModel.state

  fun displayNew(firstExerciseName: String) {
    viewModel.state = NEW
    viewModel.footerViewModel.nextExerciseName = firstExerciseName
    viewModelChengesProcessor.onNext(viewModel)
  }

  fun displayGetReady(getReadyCountDown: Int) {
    viewModel.state = GET_READY
    viewModel.bodyViewModel.countDown = getReadyCountDown
    viewModelChengesProcessor.onNext(viewModel)
  }

  fun displayPerforming(performCountDown: Int,
                        cyclesCount: Int,
                        lastCyclesCount: Int,
                        currentRoutineNr: Int,
                        routinesCount: Int,
                        currentRoutine: CyclicRoutine,
                        nextRoutine: CyclicRoutine?) {

    viewModel.state = PERFORMING
    viewModel.headerViewModel.apply {
      exerciseName = currentRoutine.exercise.name
      cycleCount = cyclesCount
      lastCycleCount = lastCyclesCount
    }
    viewModel.bodyViewModel.apply {
      countDown = performCountDown
      totalCountDown = currentRoutine.durationTimeSec
    }
    viewModel.footerViewModel.apply {
      nextRoutine?.apply { nextExerciseName = exercise.name }
      currentCount = currentRoutineNr
      totalCount = routinesCount
    }
    viewModelChengesProcessor.onNext(viewModel)
  }

  fun displayResting(restCountDown: Int, restTotalTime: Int, nextExerciseName: String) {
    viewModel.state = RESTING
    viewModel.bodyViewModel.apply {
      countDown = restCountDown
      totalCountDown = restTotalTime
    }
    viewModel.footerViewModel.nextExerciseName = nextExerciseName
    viewModelChengesProcessor.onNext(viewModel)
  }

  fun displayDone(cyclesCount: Int,
                  lastCyclesCount: Int) {
    viewModel.state = DONE
    viewModel.headerViewModel.apply {
      cycleCount = cyclesCount
      lastCycleCount = lastCyclesCount
    }
  }
}