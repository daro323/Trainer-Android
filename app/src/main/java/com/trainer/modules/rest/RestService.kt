package com.trainer.modules.rest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.os.Vibrator
import android.util.Log
import com.trainer.base.BaseApplication
import com.trainer.extensions.ioMain
import com.trainer.modules.rest.RestState.FINISHED
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by dariusz on 15/01/17.
 */
class RestService : Service() {

  @Inject lateinit var trainingManager: TrainingManager
  @Inject lateinit var vibrator: Vibrator
  @Inject lateinit var powerManager: PowerManager
  @Inject lateinit var notificationManager: RestServiceNotificationManager

  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private var restSubscription: Subscription = Subscriptions.unsubscribed()
  private var wakeLock: PowerManager.WakeLock? = null

  companion object {

    const private val TAG = "RestService"
    const private val WAKELOCK_TAG = "RestService_WakeLock"
    const private val START_REST_ACTION = "START_REST_ACTION"
    const private val STOP_REST_ACTION = "STOP_REST_ACTION"

    const private val VIBRATE_DURATION_MS = 1200L

    fun startRest(context: Context) { doStartService(context, START_REST_ACTION) }

    fun stopRest(context: Context) { doStartService(context, STOP_REST_ACTION) }

    private fun doStartService(context: Context, startAction: String) {
      Intent(context, RestService::class.java)
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
    val action = intent?.action ?: throw IllegalArgumentException("RestService started without provided action!")
    Log.d(TAG, "Rest Service was freshly started with action= $action")

    when(action) {
      START_REST_ACTION -> doStartRest()
      STOP_REST_ACTION -> doStopRest()
      else -> throw IllegalArgumentException("Unsupported RestService action= $action!")
    }

    return Service.START_REDELIVER_INTENT
  }

  override fun onDestroy() {
    restSubscription.unsubscribe()
    super.onDestroy()
  }

  private fun doStartRest() {
    require(restSubscription.isUnsubscribed) { "Subsequent call to start rest service!" }

    setWakeLockActive(true)
    restSubscription = presenter.getRestEvents()
        .ioMain()
        .doOnSubscribe { notificationManager.showNotification(this) }
        .doOnUnsubscribe { notificationManager.hideNotification() }
        .doOnNext { notificationManager.updateNotification(it.countDown, this) }
        .filter { it.state == FINISHED }
        .subscribe {
          vibrator.vibrate(VIBRATE_DURATION_MS)
          notificationManager.showForRestFinished(this)
        }

    presenter.onStartRest()
  }

  /**
   * Performed when rest is aborted by user
   */
  private fun doStopRest() {
    setWakeLockActive(false)
    restSubscription.unsubscribe()
    presenter.onStopRest()
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
}