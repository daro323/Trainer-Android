package com.trainer.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.os.ResultReceiver
import android.widget.TextView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.startServiceWith
import com.trainer.service.RestService
import com.trainer.service.RestService.Companion.RESULT_CODE_REST_FINISHED
import com.trainer.service.RestService.Companion.RESULT_CODE_REST_STARTED
import com.trainer.service.RestService.Companion.RESULT_CODE_TIME_COUNT_PROGRESS
import com.trainer.utils.bindView
import io.netopen.hotbitmapgg.library.view.RingProgressBar

class RestActivity : BaseActivity(R.layout.activity_rest) {

  private val progressView: RingProgressBar by bindView(R.id.progress_view)
  private val countDownText: TextView by bindView(R.id.countdown_text)

  private lateinit var resultReceiver: ResultReceiver
  private var restIntent: Intent? = null

  private var isActivityResumed: Boolean = false
  private var stateIsRestFinished: Boolean = false

  companion object {
    private val STATE_IS_REST_FINISHED = "STATE_IS_REST_FINISHED"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    savedInstanceState?.apply { stateIsRestFinished = getBoolean(STATE_IS_REST_FINISHED) }
  }

  override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    super.onSaveInstanceState(outState, outPersistentState)
    outState?.apply { putBoolean(STATE_IS_REST_FINISHED, stateIsRestFinished) }
  }

  override fun onStart() {
    super.onStart()
    when(stateIsRestFinished) {
      true -> {
        updateCountDown(0)
        finish()
      }
      false -> initialize()
    }
  }

  override fun onResume() {
    super.onResume()
    isActivityResumed = true
  }

  override fun onPause() {
    isActivityResumed = false
    super.onPause()
  }

  override fun onBackPressed() {
    // Block back button (just via skip can exit)
  }

  private fun tryFinish() {
    stateIsRestFinished = true
    if (isActivityResumed) finish()
  }

  private fun initialize() {
    countDownText.setOnClickListener {
      stopService(restIntent)
      finish()
    }
    startRest()
  }

  private fun onRestResult(resultCode: Int, resultData: Bundle) {
    when(resultCode) {
      RESULT_CODE_REST_STARTED -> resultData.getInt(RestService.COUNT_START_KEY).run {
        progressView.max = this
        updateCountDown(this)
      }
      RESULT_CODE_TIME_COUNT_PROGRESS -> updateCountDown(resultData.getInt(RestService.COUNT_PROGRESS_KEY))
      RESULT_CODE_REST_FINISHED -> tryFinish()
    }
  }

  private fun updateCountDown(countDown: Int) {
    progressView.progress = countDown
    countDownText.text = String.format(getString(R.string.countdown_text), countDown)
  }

  private fun startRest() {
    if (restIntent == null) {
      resultReceiver = object : ResultReceiver(Handler(mainLooper)) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) { onRestResult(resultCode, resultData) }
      }

      restIntent = startServiceWith<RestService> { putExtra(RestService.EXTRA_RECEIVER, resultReceiver) }
    }
  }
}
