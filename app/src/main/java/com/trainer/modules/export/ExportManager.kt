package com.trainer.modules.export

import android.content.Context
import android.os.Environment
import com.fasterxml.jackson.databind.ObjectMapper
import com.trainer.BuildConfig
import com.trainer.R
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.singleOnSubscribe
import com.trainer.extensions.writeString
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.TrainingPlan
import java.io.File
import javax.inject.Inject

/**
 * This class handles import & export of training plan's data
 * Created by dariusz on 12/01/17.
 */
@ApplicationScope
class ExportManager @Inject constructor(val mapper: ObjectMapper,
                                        val trainingManager: TrainingManager,
                                        @ForApplication val context: Context) {

  companion object {
    val TRAINING_EXTERNAL_STORAGE_PATH = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}${BuildConfig.REL_PATH_TO_PLANS}"

    private fun getCanonicalPlanFileName(fromName: String) = fromName
        .replace(' ', '_')
        .toLowerCase()
        .run { format(BuildConfig.PLAN_FILE_NAME_TEMPLATE, this) }

    private fun isExternalStorageWritable() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
  }

  fun exportCurrentTrainingPlan() = singleOnSubscribe { doExport() }

  fun importTrainingPlan(filePath: String) = singleOnSubscribe {
    checkExternalStorageAccess()

    doExport()
    File(filePath)
        .run { mapper.readValue(readText(), TrainingPlan::class.java) }
        .run { trainingManager.setTrainingPlan(this) }
  }

  fun getTrainingStorageDir(): File? = File(TRAINING_EXTERNAL_STORAGE_PATH)
      .apply { if (exists().not()) mkdirs() }
      .run { if (exists()) this else null }

  private fun doExport() {
    checkExternalStorageAccess()

    trainingManager.getTrainingPlan()?.apply {
      getTrainingStorageDir()
          .run { File(this, getCanonicalPlanFileName(this@apply.name)) }
          .run { writeString(mapper.writeValueAsString(this)) }
    }
  }

  private fun checkExternalStorageAccess() {
    if (isExternalStorageWritable().not()) throw Error(context.getString(R.string.failure_external_storage_not_accessible))
  }
}