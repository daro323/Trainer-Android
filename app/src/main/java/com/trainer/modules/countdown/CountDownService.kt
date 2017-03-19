package com.trainer.modules.countdown

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.trainer.base.BaseApplication
import com.trainer.commons.Lg
import javax.inject.Inject

/**
 * Can only perform one count down at a time!
 * Created by dariusz on 15/01/17.
 */
class CountDownService : Service() {

  @Inject lateinit var powerManager: PowerManager
  private var timer: CountDownTimer? = null
  private var wakeLock: PowerManager.WakeLock? = null

  companion object {
    private var callbackClient: CountDownServiceClient? = null
    private var startValue: Int? = null

    private const val WAKELOCK_TAG = "CountDownService_WakeLock"
    private const val START_COUNT_DOWN_ACTION = "START_COUNT_DOWN_ACTION"
    private const val ABORT_COUNT_DOWN_ACTION = "ABORT_COUNT_DOWN_ACTION"

    fun start(startValue: Int, callbackClient: CountDownServiceClient, context: Context) {
      this.callbackClient = callbackClient
      this.startValue = startValue
      doStartService(context, START_COUNT_DOWN_ACTION)
    }

    fun abort(context: Context) {
      callbackClient = null
      doStartService(context, ABORT_COUNT_DOWN_ACTION)
    }

    private fun doStartService(context: Context, startAction: String) {
      Intent(context, CountDownService::class.java)
          .apply { action = startAction }
          .run { context.startService(this) }
    }
  }

  override fun onCreate() {
    super.onCreate()
    BaseApplication.get(this).appComponent().inject(this)
  }

  override fun onBind(intent: Intent?) = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val action = intent?.action ?: throw IllegalArgumentException("CountDownService started without provided action!")
    Lg.d("CountDown Service was freshly started with action= $action")

    when (action) {
      START_COUNT_DOWN_ACTION -> doStartCountDown()
      ABORT_COUNT_DOWN_ACTION -> doAbortCountDown()
      else -> throw IllegalArgumentException("Unsupported CountDownService action= $action!")
    }

    return START_REDELIVER_INTENT
  }

  override fun onDestroy() {
    cleanup()
    super.onDestroy()
  }

  fun onCountDownEvents() = timer!!.onCountDownEvents()

  private fun doStartCountDown() {
    require(timer == null) { "Request to start CountDownService while service is already counting down!" }
    require(callbackClient != null && startValue != null) { "CountDownService started without required parameters!" }
    Lg.d("Start count down!")
    timer = CountDownTimer()
    callbackClient!!.onCountDownServiceReady(this)
    timer!!.start(startValue!!)
  }

  /**
   * Performed when CountDown is aborted by user
   */
  private fun doAbortCountDown() {
    Lg.d("Abort count down")
    setWakeLockActive(false)
    cleanup()
    stopSelf()
  }

  private fun setWakeLockActive(isActive: Boolean) {
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
    timer?.abort()
    timer = null
  }
}