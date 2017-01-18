package com.trainer.modules.rest

/**
 * Created by dariusz on 18/01/17.
 */

enum class RestEventType {
  IDLE,
  STARTED,
  COUNTDOWN,
  FINISHED
}

data class RestEvent (val countDown: Int,
                      val type: RestEventType)