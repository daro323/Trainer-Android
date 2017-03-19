package com.trainer.modules.countdown

/**
 * Created by dariusz on 19/03/17.
 */
enum class CountDownState {
  IDLE,
  COUNTDOWN,
  FINISHED
}

data class CountDownEvent(val countDown: Int,
                          val state: CountDownState)

interface CountDownServiceClient {
  fun onCountDownServiceReady(service: CountDownService)
}