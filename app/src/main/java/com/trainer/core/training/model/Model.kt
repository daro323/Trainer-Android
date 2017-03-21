package com.trainer.core.training.model

import android.support.annotation.Keep
import android.support.annotation.Nullable
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.trainer.core.training.model.ProgressStatus.*
import com.trainer.core.training.model.WeightType.BODY_WEIGHT
import com.trainer.core.training.model.WeightType.KG
import com.trainer.extensions.daysSince
import com.trainer.modules.init.menshilfplan.InitExerciseImageMap
import com.trainer.modules.init.menshilfplan.InitExerciseImageMap.DEFAULT_IMAGE
import com.trainer.modules.training.types.cyclic.Cycle
import com.trainer.modules.training.types.cyclic.CyclicRoutine
import com.trainer.modules.training.types.standard.Set
import com.trainer.modules.training.types.standard.SuperSet
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by dariusz on 15/03/17.
 */
@Keep
enum class SerieType {    // Add new serie types here
  SET,
  SUPER_SET,
  CYCLE
}

@Keep
enum class WeightType {
  KG,
  BODY_WEIGHT   // For this the weight value is not applicable
}

@Keep
enum class WorkoutEvent {
  REST,
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
                        val categories: kotlin.collections.Set<String>,
                        val trainingDays: MutableList<TrainingDay>) {

  fun getTrainingDay(forCategory: String) = trainingDays.find { it.category == forCategory }
}

@Keep
data class TrainingDay(val category: String, // category should be unique within a training plan
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

  override fun equals(other: Any?) = other is TrainingDay && other.category == category

  override fun hashCode() = category.hashCode().run {
    var result = 31 * this + workout.hashCode()
    result = 31 * result + totalDone
    result = 31 * result + (lastTrainedDate?.hashCode() ?: 0)
    result
  }
}

@Keep
data class Workout(val series: MutableList<Serie>) {
  fun status() = when {
    series.all { it.status() == NEW } -> NEW
    series.all { it.status() == COMPLETE } -> COMPLETE
    series.any { it.status() == STARTED || it.status() == COMPLETE } -> STARTED
    else -> throw IllegalStateException("Can't determine status of Workout= $this")
  }

  @Nullable
  fun getSerie(id: String) = series.find { it.id() == id }
}

@Keep
data class Exercise(val name: String,
                    val comments: List<String> = emptyList(),
                    val imageInfo: InitExerciseImageMap = DEFAULT_IMAGE,
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
  fun type(): SerieType

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
    fun createCycle(name: String,
                    cycleList: List<CyclicRoutine>,
                    restTimeSec: Int,
                    lastCyclesCount: Int = 0) = Cycle((++instanceCounter).toString(), name, cycleList, restTimeSec, lastCyclesCount)

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