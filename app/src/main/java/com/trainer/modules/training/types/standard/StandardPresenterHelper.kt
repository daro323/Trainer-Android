package com.trainer.modules.training.types.standard

import com.trainer.core.training.business.WorkoutPresenterHelper
import com.trainer.core.training.business.WorkoutPresenterHelper.HelperCallback
import com.trainer.core.training.model.CoreConstants.Companion.VALUE_NOT_SET
import com.trainer.core.training.model.ProgressStatus.COMPLETE
import com.trainer.core.training.model.Repetition
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.WeightType.BODY_WEIGHT
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
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  fun getSet(id: String): Set = when (serie) {
    is Set -> (serie as Set).apply { if (id() != id) throw IllegalArgumentException("Couldn't find set for ID= $id") }
    is SuperSet -> (serie as SuperSet).run { setList.find { it.id() == id } ?: throw IllegalArgumentException("Couldn't find set for ID= $id") }
    else -> throw IllegalStateException("Helper initialized with unsupported serie type= ${serie.type()}!")
  }

  fun isCurrentSet(set: Set) = if (serie.status() == COMPLETE || callback.hasOtherSerieStarted(serie)) false else getCurrentSet() == set

  fun saveSetResult(weight: Float, rep: Int) {
    val currentSet = getCurrentSet()
    val weightType = currentSet.exercise.weightType

    if (weight < 0 && weightType != BODY_WEIGHT) throw IllegalArgumentException("Missing weight value! It's expected in saveSetResult for weight type= $weightType")

    currentSet.progress.add(Repetition(weight, rep, weightType))
    if (serie is SuperSet) {
      (serie as SuperSet).setList[currentSetIdx] = currentSet
    }
    callback.onSaveSerie(serie)
  }

  fun getCurrentSet() = when (serie) {
    is Set -> serie as Set
    is SuperSet -> (serie as SuperSet).setList[currentSetIdx]
    else -> throw IllegalStateException("Helper initialized with unsupported serie type= ${serie.type()}!")
  }

  /**
   * Updates currentSetIdx in current Serie to next unfinished Set with the least progress.
   * If this is just Set then ignore.
   */
  private fun refreshCurrentSetIdx() {
    currentSetIdx = when (serie) {
      is Set -> VALUE_NOT_SET
      is SuperSet -> (serie as SuperSet).setList
          .filter { it.status() != COMPLETE }
          .sortedBy { it.progress.size }
          .first()
          .run {
            (serie as SuperSet).setList.indexOf(this)
          }
      else -> throw IllegalStateException("Can't refreshCurrentSetIdx for unsupported serie type= ${serie.type()}")
    }
  }
}