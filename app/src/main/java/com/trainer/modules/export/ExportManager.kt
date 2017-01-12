package com.trainer.modules.export

import android.os.Environment
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.trainer.BuildConfig
import com.trainer.d2.scope.ApplicationScope
import com.trainer.extensions.writeString
import com.trainer.modules.training.TrainingRepository
import java.io.File
import javax.inject.Inject


/**
 * This class handles import & export of training day's data
 * Created by dariusz on 12/01/17.
 */
@ApplicationScope
class ExportManager @Inject constructor(val repo: TrainingRepository,
                                        val mapper: ObjectMapper) {

  fun exportCurrentTrainingPlan() {   // TODO
    if (Environment.isExternalStorageWritable()) {
      val folder = getAlbumStorageDir()
      val plan = repo.getTrainingPlan()
      val planFile = File(folder, String.format(BuildConfig.PLAN_FILE_NAME_TEMPLATE, toCanonicalName(plan.name)))

      planFile.writeString(mapper.writeValueAsString(plan))
    }
  }

  fun importTrainingPlan(filePath: String) {
    exportCurrentTrainingPlan()
    // TODO
  }

  private fun getAlbumStorageDir(): File {
    val pathToFolder = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}${BuildConfig.REL_PATH_TO_PLANS}"
    val folder = File(pathToFolder)
    if (!folder.mkdirs()) {
      Log.e("ExportManager", "Directory not created")
    }
    return folder
  }

  private fun toCanonicalName(name: String) = name.replace(' ', '_')
}