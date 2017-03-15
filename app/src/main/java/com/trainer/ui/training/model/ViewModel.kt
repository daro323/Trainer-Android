package com.trainer.ui.training.model

import android.support.annotation.DrawableRes
import com.trainer.modules.training.coredata.ProgressStatus
import com.trainer.modules.training.coredata.TrainingCategory

/**
 * Created by dariusz on 06/01/17.
 */
data class TrainingDayItem(val trainingCategory: TrainingCategory,
                           val count: Int,
                           val daysAgo: Int)

data class SetItem(@DrawableRes val imageRes: Int,
                   val name: String,
                   val status: ProgressStatus)

data class SuperSetItem(@DrawableRes val imageResList: List<Int>,
                        val namesList: List<String>,
                        val status: ProgressStatus)