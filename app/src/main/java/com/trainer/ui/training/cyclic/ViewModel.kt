package com.trainer.ui.training.cyclic

import com.trainer.ui.training.cyclic.CycleState.NEW
import io.reactivex.Observable

/**
 * Created by dariusz on 18/03/17.
 */
enum class CycleState {
  NEW,        // User is presented starting screen
  GET_READY,  // Getting ready (3 to 0 count down is initiated)
  PERFORMING, // Performing a routine
  RESTING,    // Resting between routines
  DONE,       // Cycle routine set was performed, at this point we can do another cycle
  COMPLETE    // Whole cycle is finished, at this point we can't perform another cycle
}

data class CycleViewModel(var state: CycleState,
                          val headerViewModel: HeaderViewModel,
                          val bodyViewModel: BodyViewModel,
                          val footerViewModel: FooterViewModel) {
  companion object {
    fun createNew() = CycleViewModel(NEW,
        HeaderViewModel("", 0, 0),
        BodyViewModel(0),
        FooterViewModel("", 0, 0))
  }
}

data class HeaderViewModel(var exerciseName: String,
                           var cycleCount: Int,
                           var lastCycleCount: Int)

data class BodyViewModel(var countDown: Int)

data class FooterViewModel(var nextExerciseName: String,
                           var currentCount: Int,
                           var totalCount: Int)

interface CycleViewCallback {
  fun getViewModelChanges(): Observable<CycleViewModel>
  fun onCycleViewEvent(event: CycleViewEvent)
}

enum class CycleViewEvent {
  START,
  ONE_MORE,
  FINISH,
}