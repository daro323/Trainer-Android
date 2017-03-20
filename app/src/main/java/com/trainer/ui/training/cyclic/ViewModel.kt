package com.trainer.ui.training.cyclic

import com.trainer.ui.training.cyclic.CycleState.NEW

/**
 * Created by dariusz on 18/03/17.
 */
enum class CycleState {
  NEW,
  GET_READY,
  PERFORMING,
  RESTING,
  DONE
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

data class HeaderViewModel(var headerTitle: String,
                           var cycleCount: Int,
                           var lastCycleCount: Int)

data class BodyViewModel(var countDown: Int)

data class FooterViewModel(var nextExerciseName: String,
                           var currentCount: Int,
                           var totalCount: Int)