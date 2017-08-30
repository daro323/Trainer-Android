package com.trainer.ui.training.types.cyclic

import android.support.annotation.Keep
import com.trainer.modules.training.workout.types.cyclic.CycleState
import com.trainer.modules.training.workout.types.cyclic.CycleState.NEW
import io.reactivex.Observable

/**
 * Created by dariusz on 18/03/17.
 */
@Keep
data class CycleViewModel(var state: CycleState,
                          val headerViewModel: HeaderViewModel,
                          val bodyViewModel: BodyViewModel,
                          val footerViewModel: FooterViewModel,
                          var isActive: Boolean = true) {
  companion object {
    fun createNew() = CycleViewModel(NEW,
        HeaderViewModel("", 0, 0),
        BodyViewModel(0, 0),
        FooterViewModel("", 0, 0))
  }
}

@Keep
data class HeaderViewModel(var exerciseName: String,
                           var cycleCount: Int,
                           var lastCycleCount: Int)
@Keep
data class BodyViewModel(var countDown: Int,
                         var totalCountDown: Int)
@Keep
data class FooterViewModel(var nextExerciseName: String,
                           var currentCount: Int,
                           var totalCount: Int)

interface CycleViewCallback {
  fun getViewModelChanges(): Observable<CycleViewModel>
  fun onViewEvent(event: CycleViewEvent)
}

@Keep
enum class CycleViewEvent {
  START,
  ONE_MORE,
  FINISH,
}