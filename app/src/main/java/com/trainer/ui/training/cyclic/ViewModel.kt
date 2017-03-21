package com.trainer.ui.training.cyclic

import com.trainer.modules.training.types.cyclic.CycleState
import com.trainer.modules.training.types.cyclic.CycleState.NEW
import io.reactivex.Observable

/**
 * Created by dariusz on 18/03/17.
 */
data class CycleViewModel(var state: CycleState,
                          val headerViewModel: HeaderViewModel,
                          val bodyViewModel: BodyViewModel,
                          val footerViewModel: FooterViewModel) {
  companion object {
    fun createNew() = CycleViewModel(NEW,
        HeaderViewModel("", 0, 0),
        BodyViewModel(0, 0),
        FooterViewModel("", 0, 0))
  }
}

data class HeaderViewModel(var exerciseName: String,
                           var cycleCount: Int,
                           var lastCycleCount: Int)

data class BodyViewModel(var countDown: Int,
                         var totalCountDown: Int)

data class FooterViewModel(var nextExerciseName: String,
                           var currentCount: Int,
                           var totalCount: Int)

interface CycleViewCallback {
  fun getViewModelChanges(): Observable<CycleViewModel>
  fun onViewEvent(event: CycleViewEvent)
}

enum class CycleViewEvent {
  START,
  ONE_MORE,
  FINISH,
}