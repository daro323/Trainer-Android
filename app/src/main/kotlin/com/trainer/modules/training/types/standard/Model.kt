package com.trainer.modules.training.types.standard

import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import com.trainer.R
import com.trainer.core.training.model.*
import com.trainer.core.training.model.ProgressStatus.*
import com.trainer.core.training.model.SerieType.SET
import com.trainer.core.training.model.SerieType.SUPER_SET

/**
 * Created by dariusz on 15/03/17.
 */
enum class StandardStateEvent {
  DO_NEXT
}

@Keep
data class Set constructor(private val _id: String,
                           val exercise: Exercise,
                           val guidelines: List<String>,
                           val seriesCount: Int,
                           val restTimeSec: Int,
                           var progress: MutableList<Repetition>,
                           var lastProgress: List<Repetition>,
                           private val type: SerieType = SET) : Serie {

  override fun id() = _id

  override fun type() = type

  override fun status() = when {
    progress.isEmpty() -> NEW
    progress.size < seriesCount -> STARTED
    progress.size == seriesCount -> COMPLETE
    else -> throw IllegalStateException("Current progress= ${progress.size} exceeds total series count= $seriesCount")
  }

  override fun skipRemaining() {
    while (progress.size < seriesCount) progress.add(Repetition(0f, 0, exercise.weightType))
  }

  override fun completeAndReset() {
    require(progress.size == seriesCount) { "Attempt to mark set as complete when there is still some missing progress!" }
    lastProgress = progress
    progress = mutableListOf()
  }

  override fun abort() {
    progress = mutableListOf()
  }

  override fun equals(other: Any?) = other is Set && other.id() == this.id()

  override fun hashCode() = _id.hashCode().run {
    var result = 31 * this + exercise.hashCode()
    result = 31 * result + guidelines.hashCode()
    result = 31 * result + seriesCount
    result = 31 * result + restTimeSec
    result = 31 * result + progress.hashCode()
    result = 31 * result + lastProgress.hashCode()
    result
  }
}

@Keep
class SuperSet(val setList: MutableList<Set>,
               private val type: SerieType = SUPER_SET) : CompositeSerie<Set>(setList) {

  init {
    require(setList.dropLast(1).all { it.restTimeSec == 0 }) { "Inner Sets in a SuperSet shouldn't contain rest times!" }   // Verify data consistency
  }
  override fun type() = type
}

@Keep
data class StretchExercise private constructor(val id: String,
                                               val name: String,
                                               val guidelines: List<String>,
                                               val comments: List<String>,
                                               @DrawableRes val imageRes: Int) {
  companion object {
    var instanceCounter: Int = 0
    /* Automatically adds IDs as instance count */
    fun createStretch(@DrawableRes imageRes: Int = R.mipmap.ic_exercise_default,
                      guidelines: List<String> = arrayListOf("Roźciągaj 30 sekund."),
                      name: String = "Ćwiczenie",
                      comments: List<String> = emptyList()) = StretchExercise((++instanceCounter).toString(), name, guidelines, comments, imageRes)
  }
}

@Keep
data class StretchRoutine(val category: String,
                          val stretchExercises: List<StretchExercise>)

@Keep
data class StretchPlan(val stretchRoutines: List<StretchRoutine>) {

  fun getStretchRoutine(forCategory: String) = stretchRoutines.find { it.category == forCategory }
}