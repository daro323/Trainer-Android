package com.trainer.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.os.Vibrator
import com.trainer.base.BaseApplication
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import javax.inject.Inject

/**
 * Created by dariusz on 15/01/17.
 */
class RestService : IntentService("rest_time_counting_service") {

  @Inject lateinit var trainingManager: TrainingManager
  @Inject lateinit var vibrator: Vibrator

  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private lateinit var receiver: ResultReceiver
  private var shouldFinish: Boolean = false

  companion object {
    val EXTRA_RECEIVER = "EXTRA_RECEIVER"
    val RESULT_CODE_REST_STARTED = 444
    val RESULT_CODE_TIME_COUNT_PROGRESS = 555
    val RESULT_CODE_REST_FINISHED = 666

    val COUNT_PROGRESS_KEY = "COUNT_PROGRESS_KEY"     // contains progress as Int
    val COUNT_START_KEY = "COUNT_START_KEY"           // contains starting value as Int
    val COUNT_FINISH_KEY = "COUNT_FINISH_KEY"         // indicates count finished
  }

  override fun onHandleIntent(intent: Intent?) {
    receiver = intent?.getParcelableExtra(EXTRA_RECEIVER) ?: throw IllegalArgumentException("Attempt to start RestService without ResultReceiver being passed!")
    startRestCountDown()
  }

  override fun onCreate() {
    super.onCreate()
    BaseApplication.get(this).appComponent().inject(this)
  }

  override fun onDestroy() {
    shouldFinish = true
    super.onDestroy()
  }

  private fun startRestCountDown() {
    var counter = 0
    presenter.getRestTime().run {
      require(this > 0) { "RestService called for not rest time set!" }

      sendStarted(this)
      do {
        if (shouldFinish) return

        sendProgress(this - counter++)
        Thread.sleep(1000)
      } while (counter != this)
      sendFinished()
    }
  }

  private fun sendStarted(restTime: Int) { receiver.send(RESULT_CODE_REST_STARTED, Bundle().apply { putInt(COUNT_START_KEY, restTime) }) }
  private fun sendProgress(progress: Int) { receiver.send(RESULT_CODE_TIME_COUNT_PROGRESS, Bundle().apply { putInt(COUNT_PROGRESS_KEY, progress) }) }
  private fun sendFinished() {
    vibrator.vibrate(1000)
    receiver.send(RESULT_CODE_REST_FINISHED, Bundle().apply { putBoolean(COUNT_FINISH_KEY, true) })
  }
}