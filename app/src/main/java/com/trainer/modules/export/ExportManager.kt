package com.trainer.modules.export

import com.trainer.d2.scope.ApplicationScope
import javax.inject.Inject

/**
 * This class handles import & export of training day's data
 * Created by dariusz on 12/01/17.
 */
@ApplicationScope
class ExportManager @Inject constructor() {

  companion object {
    private val EXPORT_FILE_NAME_TEMPLATE = "%s.pln"
  }

  fun exportCurrentTrainingPlan() {

  }

  fun importTrainingPlan(fileName: String) {

  }
}