package com.trainer.modules.training

import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.SerieType.*
import com.trainer.modules.training.WorkoutPresenterHelper.HelperCallback
import com.trainer.modules.training.types.cyclic.CyclicPresenterHelper
import com.trainer.modules.training.types.standard.StandardPresenterHelper
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by dariusz on 16/03/17.
 */
@ApplicationScope
class PresenterHelperFactory @Inject constructor(private val standardHelperProvider: Provider<StandardPresenterHelper>,
                                                 private val cycleHelperPresenter: Provider<CyclicPresenterHelper>) {

  fun getHelperFor(serie: Serie, callback: HelperCallback): WorkoutPresenterHelper = when (serie.type()) {
    SET, SUPER_SET -> standardHelperProvider.get().apply { initWith(serie, callback) }
    CYCLE -> cycleHelperPresenter.get().apply { initWith(serie, callback) }
    else -> throw IllegalAccessException("Unsupported serie type= ${serie.type()}")
  }
}