package com.trainer.modules.rest

/**
 * Created by dariusz on 18/01/17.
 */

enum class RestEventType {
  STARTED,
  COUNTDOWN,
  FINISHED,
  ABORTED
}

data class RestEvent (val startValue: Int,
                      val countDown: Int,
                      val type: RestEventType)