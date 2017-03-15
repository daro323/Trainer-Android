package com.trainer.commons

import com.trainer.R
import com.trainer.modules.training.coredata.ProgressStatus

/**
 * Created by dariusz on 12/01/17.
 */
class StyleUtils private constructor() {
  companion object {

    fun getColorRes(forProgressStatus: ProgressStatus) =
        when (forProgressStatus) {
          ProgressStatus.NEW -> R.color.white
          ProgressStatus.STARTED -> R.color.yellow
          ProgressStatus.COMPLETE -> R.color.green
        }
  }
}