package com.trainer.utils

import com.trainer.R
import com.trainer.modules.training.ProgressStatus

/**
 * Created by dariusz on 12/01/17.
 */
class TrainingUtils private constructor() {
  companion object {

    fun getColorRes(forProgressStatus: ProgressStatus) =
        when (forProgressStatus) {
          ProgressStatus.NEW -> R.color.white
          ProgressStatus.STARTED -> R.color.yellow
          ProgressStatus.COMPLETE -> R.color.green
        }
  }
}