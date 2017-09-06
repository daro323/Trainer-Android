package com.trainer.modules.training.workout

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import android.support.annotation.Nullable
import com.fasterxml.jackson.annotation.JsonProperty
import com.trainer.R
import com.trainer.extensions.daysSince
import com.trainer.modules.training.workout.ProgressStatus.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by dariusz on 15/03/17.
 */
@Keep
enum class SerieType {    // Add new serie types here
  @JsonProperty("SET")
  SET,
  @JsonProperty("SUPER_SET")
  SUPER_SET,
  @JsonProperty("CYCLE")
  CYCLE
}

@Keep
enum class WeightType {
  @JsonProperty("KG")
  KG,
  @JsonProperty("BODY_WEIGHT")
  BODY_WEIGHT   // For this the weight value is not applicable
}

@Keep
enum class WorkoutEvent {
  ONGOING,
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
@Entity(tableName = "TrainingPlans")
data class TrainingPlan(@PrimaryKey val id: Int,
                        val name: String,
                        val categories: kotlin.collections.Set<String>)

@Keep
@Entity(
    tableName = "TrainingDays",
    primaryKeys = arrayOf("category", "trainingPlanId"),
    foreignKeys = arrayOf(ForeignKey(entity = TrainingPlan::class, onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("trainingPlanId"))))
data class TrainingDay(val category: String, // category should be unique within a training plan
                       val trainingPlanId: Int,
                       val workout: Workout,
                       var totalDone: Int = 0,
                       var lastTrainedDate: String? = null) {

  companion object {
    private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
  }

  fun updateAsDone() {
    totalDone++
    lastTrainedDate = LocalDate.now().format(DATE_FORMATTER)
  }

  fun trainedDaysAgo() = LocalDate.now().daysSince(lastTrainedDate?.run { LocalDate.parse(this, DATE_FORMATTER) } ?: LocalDate.now()).toInt()

  override fun equals(other: Any?) = other is TrainingDay &&
      other.category == category && other.trainingPlanId == trainingPlanId

  override fun hashCode() = 31 * category.hashCode() + trainingPlanId.hashCode()
}

@Keep
@Entity(tableName = "Exercises")
data class Exercise(@PrimaryKey val id: Int,
                    val name: String,
                    val comments: List<String> = emptyList(),
                    val weightType: WeightType = WeightType.KG) {
  companion object {
    @DrawableRes
    fun getImageResource() = R.mipmap.ic_exercise_default
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
data class Repetition(val weight: Float,
                      val repCount: Int) {

  companion object {
    fun EMPTY() = Repetition(0f, 0)
  }

  fun formatFor(weightType: WeightType) = when (weightType) {
    WeightType.BODY_WEIGHT -> "$repCount reps"
    else -> "$weight $weightType  [x]  $repCount"
  }
}

@Keep
interface Serie {
  fun id(): String
  fun name(): String
  fun status(): ProgressStatus
  fun skipRemaining()
  fun completeAndReset()
  fun abort()
  fun type(): SerieType
}

@Keep
abstract class CompositeSerie<T : Serie> constructor(val name: String,
                                                     val seriesList: MutableList<T>) : Serie {
  override fun id() = seriesList
      .map(Serie::id)
      .reduce { acc, item -> "$acc$item" }

  override fun name() = name

  override fun status() = when {
    seriesList.all { it.status() == NEW } -> NEW
    seriesList.all { it.status() == COMPLETE } -> COMPLETE
    seriesList.any { it.status() == STARTED || it.status() == COMPLETE } -> STARTED
    else -> throw IllegalStateException("Can't determine status of SuperSet= $this")
  }

  override fun skipRemaining() = seriesList.forEach(Serie::skipRemaining)

  override fun completeAndReset() = seriesList.forEach(Serie::completeAndReset)

  override fun abort() = seriesList.forEach(Serie::abort)
}