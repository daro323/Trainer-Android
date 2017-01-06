package com.trainer.modules.training

import rx.Observable
import javax.inject.Inject

/**
 * Created by dariusz on 06/01/17.
 */
class TrainingManager @Inject constructor(val repo: TrainingRepository) {

  fun getAllTrainingDaysData(): Observable<List<TrainingDay>> {
    return Observable.from(Training.values())
        .map { repo.getTrainingDay(it) }
        .toList()
  }
}