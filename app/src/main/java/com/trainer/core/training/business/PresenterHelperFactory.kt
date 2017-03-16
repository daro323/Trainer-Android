package com.trainer.core.training.business

import com.trainer.core.training.business.WorkoutPresenterHelper.HelperCallback
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.SerieType.*
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.cyclic.CyclicPresenterHelper
import com.trainer.modules.training.standard.StandardPresenterHelper
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