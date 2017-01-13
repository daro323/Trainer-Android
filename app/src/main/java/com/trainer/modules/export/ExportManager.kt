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
import java.io.File
import javax.inject.Inject

/**
 * This class handles import & export of training day's data
 * Created by dariusz on 12/01/17.
 */
@ApplicationScope
class ExportManager @Inject constructor(val mapper: ObjectMapper,
                                        val trainingManager: TrainingManager,
                                        @ForApplication val context: Context) {

  fun exportCurrentTrainingPlan() = singleOnSubscribe { doExport() }

  fun importTrainingPlan(filePath: String) = singleOnSubscribe {
    if (isExternalStorageWritable().not()) throw Error(context.getString(R.string.failure_external_storage_not_accessible))

    doExport()
  }

  private fun doExport() {
    if (isExternalStorageWritable().not()) throw Error(context.getString(R.string.failure_external_storage_not_accessible))

    val folder = getTrainingStorageDir()
    trainingManager.getTrainingPlan()?.apply {
      val planFile = File(folder, String.format(BuildConfig.PLAN_FILE_NAME_TEMPLATE, toCanonicalName(this.name)))
      planFile.writeString(mapper.writeValueAsString(this))
    }
  }

  private fun getTrainingStorageDir(): File {
    val pathToFolder = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}${BuildConfig.REL_PATH_TO_PLANS}"
    val folder = File(pathToFolder)

    if (folder.mkdirs().not()) throw Error("Failed to create training storage external folder")

    return folder
  }

  private fun toCanonicalName(name: String) = name.replace(' ', '_').toLowerCase()

  private fun isExternalStorageWritable() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}