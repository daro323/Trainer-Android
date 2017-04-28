package com.trainer.core.training.business

import com.trainer.core.training.model.Serie

/**
 * Created by dariusz on 15/03/17.
 */
interface WorkoutPresenterHelper {
  fun initWith(serie: Serie, callback: HelperCallback)

  fun getRestTime(): Int

  fun getSerie(): Serie

  fun onSkipSerie()

  /**
   * Should only be called when resting is done and both workout and current serie are NOT finished
   */
  fun determineNextStep()

  interface HelperCallback {
    fun onSaveSerie(serie: Serie, skipRest: Boolean = false)

    fun hasOtherSerieStarted(thanSerie: Serie): Boolean
  }
}