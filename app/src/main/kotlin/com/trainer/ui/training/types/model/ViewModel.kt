package com.trainer.ui.training.types.model

import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import com.trainer.modules.training.ProgressStatus

/**
 * Created by dariusz on 06/01/17.
 */
@Keep
data class TrainingDayItem(val trainingCategory: String,
                           val count: Int,
                           val daysAgo: Int)
@Keep
data class SetItem(val id: String,
                   @DrawableRes val imageRes: Int,
                   val name: String,
                   val status: ProgressStatus)
@Keep
data class SuperSetItem(val id: String,
                        @DrawableRes val imageResList: List<Int>,
                        val namesList: List<String>,
                        val status: ProgressStatus)
@Keep
data class CycleItem(val id: String,
                     val name: String,
                     val status: ProgressStatus)