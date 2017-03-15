package com.trainer.modules.training.coredata

import android.support.annotation.Keep
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.trainer.extensions.daysSince
import com.trainer.modules.training.coredata.ProgressStatus.*
import com.trainer.modules.training.coredata.WeightType.BODY_WEIGHT
import com.trainer.modules.training.coredata.WeightType.KG
import com.trainer.modules.training.cyclic.Cycle
import com.trainer.modules.training.cyclic.CyclicRoutine
import com.trainer.modules.training.standard.ExerciseImageMap
import com.trainer.modules.training.standard.Set
import com.trainer.modules.training.standard.SuperSet
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by dariusz on 15/03/17.
 */
@Keep
enum class WeightType {
  KG,
  BODY_WEIGHT   // For this the weight value is not applicable
}

@Keep
enum class TrainingCategory {
  CHEST,
  BACK,
  SHOULDERS,
  ARMS,
  LEGS
}

@Keep
enum class WorkoutEvent {
  REST,
  DO_NEXT,          // do whatever is next either it's next serie or something within current serie (like Set in SuperSet)
  SERIE_COMPLETED,
  WORKOUT_COMPLETED
}

@Keep
enum class ProgressStatus {
  NEW,
  STARTED,
  COMPLETE
}

class CoreConstants private constructor() {
  companion object {
    const val WEIGHT_VALUE_NOT_APPLICABLE = -1f   // value for weight which is considered not applicable
    const val VALUE_NOT_SET = -1
  }
}

@Keep
data class TrainingPlan(val name: String,
                        val trainingDays: List<TrainingDay>) {

  fun getTrainingDay(forCategory: TrainingCategory) = trainingDays.find { it.category == forCategory }
}

@Keep
data class TrainingDay(val category: TrainingCategory,
                       val workout: Workout,
                       private var totalDone: Int = 0,
                       private var lastTrainedDate: String? = null) {

  companion object {
    private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
  }

  fun updateAsDone() {
    totalDone++
    lastTrainedDate = LocalDate.now().format(DATE_FORMATTER)
  }

  fun getTotalDone() = totalDone

  fun trainedDaysAgo() = LocalDate.now().daysSince(lastTrainedDate?.run { LocalDate.parse(this, DATE_FORMATTER) } ?: LocalDate.now()).toInt()
}

@Keep
data class Workout(val series: List<Serie>) {
  fun status() = when {
    series.all { it.status() == NEW } -> NEW
    series.all { it.status() == COMPLETE } -> COMPLETE
    series.any { it.status() == STARTED || it.status() == COMPLETE } -> STARTED
    else -> throw IllegalStateException("Can't determine status of Workout= $this")
  }
}

@Keep
data class Exercise(val name: String,
                    val comments: List<String> = emptyList(),
                    val imageInfo: ExerciseImageMap = ExerciseImageMap.DEFAULT_IMAGE,
                    val weightType: WeightType = KG) {

  fun imageResource() = imageInfo.resource
}

@Keep
data class Repetition(val weight: Float,
                      val repCount: Int,
                      val weightType: WeightType) {

  override fun toString() = when (weightType) {
    BODY_WEIGHT -> "$repCount reps"
    else -> "$weight $weightType  [x]  $repCount"
  }
}

@Keep
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Set::class, name = "Set"),
    JsonSubTypes.Type(value = SuperSet::class, name = "SuperSet"),
    JsonSubTypes.Type(value = Cycle::class, name = "Cycle"))
interface Serie {
  fun id(): String
  fun status(): ProgressStatus
  fun skipRemaining()
  fun complete()
  fun abort()

  companion object {
    var instanceCounter: Int = 0
    /* Automatically adds IDs as instance count */
    fun createSet(exercise: Exercise,
                  guidelines: List<String>,
                  seriesCount: Int,
                  restTimeSec: Int,
                  progress: MutableList<Repetition> = mutableListOf(),
                  lastProgress: List<Repetition> = (1..seriesCount).map { emptyRepetition(exercise) }.toList()) = Set((++instanceCounter).toString(), exercise, guidelines, seriesCount, restTimeSec, progress, lastProgress)

    /* Automatically adds IDs as instance count */
    fun createCycle(cycleList: List<CyclicRoutine>,
                    totalCycles: Int,
                    restTime: Int,
                    cyclesCount: Int = -1,
                    lastCyclesCount: Int = 0) = Cycle((++instanceCounter).toString(), cycleList, totalCycles, cyclesCount, lastCyclesCount, restTime)

    private fun emptyRepetition(forExercise: Exercise) = Repetition(0f, 0, forExercise.weightType)
  }
}

@Keep
abstract class CompositeSerie<out T : Serie> constructor(val seriesList: List<T>) : Serie {
  override fun id() = seriesList
      .map(Serie::id)
      .reduce { acc, item -> "$acc$item" }

  override fun status() = when {
    seriesList.all { it.status() == NEW } -> NEW
    seriesList.all { it.status() == COMPLETE } -> COMPLETE
    seriesList.any { it.status() == STARTED || it.status() == COMPLETE } -> STARTED
    else -> throw IllegalStateException("Can't determine status of SuperSet= $this")
  }

  override fun skipRemaining() = seriesList.forEach(Serie::skipRemaining)

  override fun complete() = seriesList.forEach(Serie::complete)

  override fun abort() = seriesList.forEach(Serie::abort)
}