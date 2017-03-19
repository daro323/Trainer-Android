package com.trainer.modules.training.rest

/**
 * Created by dariusz on 18/01/17.
 */
enum class RestState {
  START,
  RESTING,
  FINISHED,
  ABORTED
}

data class RestEvent(val countDown: Int,
                     val state: RestState)