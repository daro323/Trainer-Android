package com.trainer.modules.training.types.standard

import com.trainer.modules.training.WorkoutPresenterHelper
import com.trainer.modules.training.WorkoutPresenterHelper.HelperCallback
import com.trainer.modules.training.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.modules.training.ProgressStatus.COMPLETE
import com.trainer.modules.training.Repetition
import com.trainer.modules.training.Serie
import com.trainer.modules.training.WeightType.BODY_WEIGHT
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

/**
 * Initialized with either Set or SuperSet
 *
 * Created by dariusz on 15/03/17.
 */
class StandardPresenterHelper @Inject constructor() : WorkoutPresenterHelper {

  private lateinit var serie: Serie
  private lateinit var callback: HelperCallback
  private var currentSetIdx = VALUE_NOT_SET
  private val standardStateEventsProcessor = BehaviorProcessor.create<StandardStateEvent>()

  companion object {
    const val DEFAULT_SET_INDEX = 0
  }

  override fun initWith(serie: Serie, callback: HelperCallback) {
    require(serie is Set || serie is SuperSet) { "Unsupported Serie type= ${serie.javaClass.name}" }
    this.serie = serie
    this.callback = callback
    if (serie.status() != COMPLETE) refreshCurrentSetIdx() else currentSetIdx = DEFAULT_SET_INDEX
  }

  override fun getRestTime() = getCurrentSet().restTimeSec

  override fun getSerie() = serie

  override fun determineNextStep() {
    refreshCurrentSetIdx()
    standardStateEventsProcessor.onNext(StandardStateEvent.DO_NEXT)
  }

  override fun onSkipSerie() {
    // Ignore...
  }

  fun getSet(id: String): Set = when (serie) {
    is Set -> (serie as Set).apply { if (id() != id) throw IllegalArgumentException("Couldn't find set for ID= $id") }
    is SuperSet -> (serie as SuperSet).run { seriesList.find { it.id() == id } ?: throw IllegalArgumentException("Couldn't find set for ID= $id") }
    else -> throw IllegalStateException("Helper initialized with unsupported serie type= ${serie.type()}!")
  }

  fun isCurrentSet(set: Set) = if (serie.status() == COMPLETE || callback.hasOtherSerieStarted(serie)) false else getCurrentSet() == set

  fun saveSetResult(weight: Float, rep: Int) {
    val currentSet = getCurrentSet()
    val weightType = currentSet.exercise.weightType

    if (weight < 0 && weightType != BODY_WEIGHT) throw IllegalArgumentException("Missing weight value! It's expected in saveSetResult for weight type= $weightType")

    currentSet.progress.add(Repetition(weight, rep, weightType))
    if (serie is SuperSet) {
      (serie as SuperSet).seriesList[currentSetIdx] = currentSet
    }
    callback.onSaveSerie(serie)
  }

  fun getCurrentSet() = when (serie) {
    is Set -> serie as Set
    is SuperSet -> (serie as SuperSet).seriesList[currentSetIdx]
    else -> throw IllegalStateException("Helper initialized with unsupported serie type= ${serie.type()}!")
  }

  fun getStandardStateEvents() = standardStateEventsProcessor.toObservable()

  /**
   * Updates currentSetIdx in current Serie to next unfinished Set with the least progress.
   * If this is just Set then ignore.
   */
  private fun refreshCurrentSetIdx() {
    currentSetIdx = when (serie) {
      is Set -> VALUE_NOT_SET
      is SuperSet -> (serie as SuperSet).seriesList
          .filter { it.status() != COMPLETE }
          .sortedBy { it.progress.size }
          .first()
          .run {
            (serie as SuperSet).seriesList.indexOf(this)
          }
      else -> throw IllegalStateException("Can't refreshCurrentSetIdx for unsupported serie type= ${serie.type()}")
    }
  }
}