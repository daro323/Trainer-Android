package com.trainer.modules.export

import android.app.Application
import android.os.Environment
import com.fasterxml.jackson.databind.ObjectMapper
import com.trainer.BuildConfig
import com.trainer.R
import com.trainer.extensions.writeString
import com.trainer.modules.training.workout.WorkoutManager
import com.trainer.persistence.training.TrainingPlanDao
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class handles import & export of training plan's data
 * Created by dariusz on 12/01/17.
 */
@Singleton
class ExportManager @Inject constructor(val mapper: ObjectMapper,
                                        val workoutManager: WorkoutManager,
                                        val context: Application) {

  companion object {
    val TRAINING_EXTERNAL_STORAGE_PATH = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}${BuildConfig.REL_PATH_TO_PLANS}"

    private fun getCanonicalPlanFileName(fromName: String) = fromName
        .replace(' ', '_')
        .toLowerCase()
        .run { format(BuildConfig.PLAN_FILE_NAME_TEMPLATE, this) }

    private fun isExternalStorageWritable() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
  }

  fun exportCurrentTrainingPlan() = Completable.fromAction { doExport() }

  fun importTrainingPlan(filePath: String) = Completable.fromAction {
    checkExternalStorageAccess()

    File(filePath)
        .run { mapper.readValue(readText(), TrainingPlanDao.TrainingPlan::class.java) }
        .run { workoutManager.setTrainingPlan(this) }
  }

  fun getTrainingStorageDir(): File? = File(TRAINING_EXTERNAL_STORAGE_PATH)
      .apply { if (exists().not()) mkdirs() }
      .run { if (exists()) this else null }

  private fun doExport() {
    checkExternalStorageAccess()

    workoutManager.getTrainingPlan().apply {
      getTrainingStorageDir()
          .run { File(this, getCanonicalPlanFileName(this@apply.name)) }
          .run { writeString(mapper.writeValueAsString(this@apply)) }
    }
  }

  private fun checkExternalStorageAccess() {
    if (isExternalStorageWritable().not()) throw Error(context.getString(R.string.failure_external_storage_not_accessible))
  }
}