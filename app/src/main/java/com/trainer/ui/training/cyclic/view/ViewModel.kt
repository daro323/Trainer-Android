package com.trainer.ui.training.cyclic.view

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
                          val footerViewModel: FooterViewModel)

data class HeaderViewModel(var headerTitle: String,
                           var cycleCount: Int,
                           val lastCycleCount: Int)

data class BodyViewModel(var countDown: Int)

data class FooterViewModel(var nextExerciseName: String,
                           var currentCount: Int,
                           var totalCount: Int)