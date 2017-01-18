package com.trainer.modules.rest

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.TrainingRepository
import rx.Observable
import javax.inject.Inject

/**
 * Created by dariusz on 18/01/17.
 */
@ApplicationScope
class RestManager @Inject constructor(val repo: TrainingRepository) {

  fun startRest(): Observable<RestEvent> {
    // TODO
  }

  fun continueRest(): Observable<RestEvent> {
    // TODO
  }

  fun stopRest() {
    // TODO
  }
}