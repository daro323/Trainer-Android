package com.trainer.modules.rest

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.trainer.R
import com.trainer.base.BaseApplication
import com.trainer.extensions.ioMain
import com.trainer.modules.rest.RestEventType.ABORTED
import com.trainer.modules.rest.RestEventType.FINISHED
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.ui.RestActivity
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by dariusz on 15/01/17.
 */
class RestService : Service() {

  @Inject lateinit var trainingManager: TrainingManager
  @Inject lateinit var restManager: RestManager
  @Inject lateinit var vibrator: Vibrator
  @Inject lateinit var notificationManager: NotificationManager

  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private var restSubscription: Subscription = Subscriptions.unsubscribed()
  private lateinit var notificationBuilder: NotificationCompat.Builder

  companion object {

    const private val TAG = "RestService"
    const private val START_REST_ACTION = "START_REST_ACTION"
    const private val STOP_REST_ACTION = "STOP_REST_ACTION"
    const private val NOTIFICATION_ID = 997

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
    setupNotification()
  }

  override fun onBind(intent: Intent?) = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (intent == null) {
      Log.d(TAG, "Rest Service was killed and recreated - continuing rest time...")
      doResumeRest()
    } else {
      val action = intent.action
      require(action != null) { "RestService started without provided action!" }
      Log.d(TAG, "Rest Service was freshly started with action= $action")

      when(action) {
        START_REST_ACTION -> doStartRest()
        STOP_REST_ACTION -> doStopRest()
        else -> throw IllegalArgumentException("Unsupported RestService action= $action!")
      }
    }

    return Service.START_STICKY
  }

  override fun onDestroy() {
    restSubscription.unsubscribe()
    super.onDestroy()
  }

  private fun doStartRest() {
    require(restSubscription.isUnsubscribed) { "Subsequent call to start rest service!" }

    restSubscription = restManager.startRest()
        .ioMain()
        .doOnNext { presenter.updateRest(it) }
        .subscribe { if (it.type == FINISHED || it.type == ABORTED) doFinishRest(it.type == FINISHED) }
    showNotification()
  }

  private fun doResumeRest() {
    restSubscription.unsubscribe()
    restSubscription = restManager.continueRest()
        .ioMain()
        .doOnNext { presenter.updateRest(it) }
        .subscribe { if (it.type == FINISHED || it.type == ABORTED) doFinishRest(it.type == FINISHED) }
    showNotification()
  }

  /**
   * Performed when rest is aborted by user
   */
  private fun doStopRest() {
    restManager.stopRest()
  }

  /**
   * Performed when rest count down reaches 0
   */
  private fun doFinishRest(vibrate: Boolean = true) {
    restSubscription.unsubscribe()
    if (vibrate) vibrator.vibrate(100)
    hideNotification()
    stopSelf()
  }

  private fun showNotification() {
    startForeground(NOTIFICATION_ID, notificationBuilder.build())
  }

  private fun hideNotification() {
    notificationManager.cancel(NOTIFICATION_ID)
  }

  // TODO: Update notification to show rest time countdown
  private fun setupNotification() {
    val largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.icon_rest_notification)
    val intent = Intent(applicationContext, RestActivity::class.java)
        .run { PendingIntent.getActivity(applicationContext, 111, this, PendingIntent.FLAG_UPDATE_CURRENT) }

    notificationBuilder = NotificationCompat.Builder(applicationContext)
        .setLargeIcon(largeIcon)
        .setContentIntent(intent)
        .setContentTitle(applicationContext.getString(R.string.rest_notification_title))
        .setSmallIcon(R.drawable.ic_small_rest_notification)
        .setAutoCancel(false)
  }
}