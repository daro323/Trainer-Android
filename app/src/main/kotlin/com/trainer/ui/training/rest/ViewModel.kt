package com.trainer.ui.training.rest

import android.support.annotation.Keep

/**
 * Created by dariusz on 03.05.17.
 */
@Keep
data class ProgressItem(val name: String,
                        val isCurrent: Boolean = false)