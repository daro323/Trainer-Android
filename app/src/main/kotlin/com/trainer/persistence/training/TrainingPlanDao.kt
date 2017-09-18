package com.trainer.persistence.training

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import com.trainer.R
import com.trainer.extensions.daysSince
import com.trainer.modules.training.workout.WeightType
import com.trainer.modules.training.workout.Workout
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by dariusz on 29.08.17.
 */
@Dao
interface TrainingPlanDao {

  @Query("SELECT * FROM TrainingPlans")
  fun getAllPlans(): List<TrainingPlan>

  @Query("SELECT * FROM TrainingPlans")
  fun getAllPlansStream(): LiveData<List<TrainingPlan>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addPlan(planEntity: TrainingPlan)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addPlans(plansEntityList: List<TrainingPlan>)

  @Delete
  fun removePlan(planEntity: TrainingPlan)

  @Update
  fun updatePlan(planEntity: TrainingPlan)


  @Keep
  @Entity(tableName = "TrainingPlans")
  data class TrainingPlan(@PrimaryKey val id: String,
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
  data class Exercise(@PrimaryKey val id: String,
                      val name: String,
                      val comments: List<String> = emptyList(),
                      val weightType: WeightType = WeightType.KG) {
    companion object {
      @DrawableRes
      fun getImageResource() = R.mipmap.ic_exercise_default
    }
  }
}