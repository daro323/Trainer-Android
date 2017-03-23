package com.trainer.modules.countdown

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.support.v4.content.LocalBroadcastManager
import com.trainer.base.BaseApplication
import com.trainer.commons.Lg
import com.trainer.extensions.with
import javax.inject.Inject

/**
 * Can only perform one count down at a time!
 * Needs to be explicitly started, aborted, finished (as there is also companion notification in the status bar)
 *
 * Created by dariusz on 15/01/17.
 */
class CountDownService : Service() {

  @Inject lateinit var powerManager: PowerManager
  private var timer: CountingDownTimer? = null
  private var wakeLock: PowerManager.WakeLock? = null

  companion object {
    const val COUNT_DOWN_EVENT_ACTION = "COUNT_DOWN_EVENT_ACTION"
    const val COUNT_DOWN_EVENT_ARG = "COUNT_DOWN_EVENT_ARG"

    private const val WAKELOCK_TAG = "CountDownService_WakeLock"
    private const val START_COUNT_DOWN_ACTION = "START_COUNT_DOWN_ACTION"
    private const val ABORT_COUNT_DOWN_ACTION = "ABORT_COUNT_DOWN_ACTION"
    private const val FINISH_COUNT_DOWN_ACTION = "FINISH_COUNT_DOWN_ACTION"
    private const val START_VALUE_ARG = "START_VALUE_ARG"

    fun start(startValue: Int, context: Context, onCountDownAction: (Int) -> Unit): CountDownReceiver {
      startWith(context, START_COUNT_DOWN_ACTION) { putExtra(START_VALUE_ARG, startValue) }
      return CountDownReceiver(onCountDownAction)
    }

    fun abort(context: Context) {
      startWith(context, ABORT_COUNT_DOWN_ACTION)
    }

    fun finish(context: Context) {
      startWith(context, FINISH_COUNT_DOWN_ACTION)
    }

    fun startWith(context: Context, startAction: String, intentSetup: (Intent.() -> Unit)? = null) {
      Intent(context, CountDownService::class.java)
          .apply {
            action = startAction
            intentSetup?.invoke(this)
          }
          .run { context.startService(this) }
    }
  }

  override fun onCreate() {
    super.onCreate()
    BaseApplication.get(this).appComponent().inject(this)
  }

  override fun onBind(intent: Intent?) = null

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val action = intent.action ?: throw IllegalArgumentException("CountDownService started without provided action!")
    Lg.d("CountDown Service was freshly started with action= $action")

    when (action) {
      START_COUNT_DOWN_ACTION -> doStartCountDown(intent.getIntExtra(START_VALUE_ARG, -1))
      ABORT_COUNT_DOWN_ACTION, FINISH_COUNT_DOWN_ACTION -> doAbortCountDown()
      else -> throw IllegalArgumentException("Unsupported CountDownService action= $action!")
    }

    return START_REDELIVER_INTENT
  }

  private fun doStartCountDown(startValue: Int) {
    require(timer == null) { "Request to start CountDownService while service is already counting down!" }
    require(startValue > 0) { "Request to start CountDownService without providing a valid startValue! (was= $startValue)" }

    Lg.d("Start count down!")
    setWakeLockActive(true)
    timer = CountingDownTimer().apply {
      start(startValue)
          .subscribe {
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(
                Intent().with(COUNT_DOWN_EVENT_ACTION) { putExtra(COUNT_DOWN_EVENT_ARG, it) })
          }
    }
  }

  /**
   * Performed when CountDown is aborted by user
   */
  private fun doAbortCountDown() {
    Lg.d("Abort count down")
    cleanup()
    stopSelf()
  }

  private fun setWakeLockActive(isActive: Boolean) {
    Lg.d("setWakeLockActive to $isActive")
    if (isActive) {
      require(wakeLock == null) { "Request to acquire wake lock but it's already acquired!" }
      wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG).apply { acquire() }
    } else {
      require(wakeLock != null) { "Request to free wake lock but there is non acquired!" }
      wakeLock = wakeLock!!.run {
        release()
        null
      }
    }
  }

  private fun cleanup() {
    Lg.d("cleanup count down!")
    setWakeLockActive(false)
    timer?.abort()
    timer = null
  }
}