package com.trainer.ui.training.model

import android.support.annotation.DrawableRes
import com.trainer.core.training.model.ProgressStatus

/**
 * Created by dariusz on 06/01/17.
 */
data class TrainingDayItem(val trainingCategory: String,
                           val count: Int,
                           val daysAgo: Int)

data class SetItem(val id: String,
                   @DrawableRes val imageRes: Int,
                   val name: String,
                   val status: ProgressStatus)

data class SuperSetItem(val id: String,
                        @DrawableRes val imageResList: List<Int>,
                        val namesList: List<String>,
                        val status: ProgressStatus)