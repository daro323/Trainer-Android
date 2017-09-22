package com.trainer.modules.countdown

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.support.annotation.DrawableRes
import android.support.annotation.Keep
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import com.trainer.R
import paperparcel.PaperParcel

/**
 * Created by dariusz on 15.04.17.
 */
class CountDownNotification {
  private val context: Context
  private val notificationId: Int
  private val notificationBuilder: NotificationCompat.Builder
  private val notificationManager: NotificationManager

  constructor(initData: InitData, context: Application, notificationManager: NotificationManager) {
    this.context = context
    this.notificationId = initData.notificationId
    this.notificationManager = notificationManager

    notificationBuilder = NotificationCompat.Builder(context)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, initData.largeIconDrawable))
        .setContentIntent(
            Intent(context, Class.forName(initData.pendingIntentClassName))
                .run { PendingIntent.getActivity(context, initData.pendingIntentRequestCode, this, PendingIntent.FLAG_UPDATE_CURRENT) })
        .setSmallIcon(initData.smallIconDrawable)
        .setContentTitle(context.getString(initData.title))
        .setAutoCancel(false)
  }

  companion object {
    const val REST_NOTIFICATION_ID = 998
    const val CYCLE_ROUTINE_REST_NOTIFICATION_ID = 997
    const val PERFORMING_NOTIFICATION_ID = 996

    const val DUMMY_REQUESTCODE = 111
  }

  fun showNotification(restTime: Int, service: Service) {
    if (restTime > 0) {
      service.startForeground(notificationId, notificationBuilder.run {
        setContentText(String.format(context.getString(R.string.notification_countdown_text), restTime))
        build()
      })
    } else {
      showForPerformFinished(service)
    }
  }

  fun hideNotification() {
    notificationManager.cancel(notificationId)
  }

  private fun showForPerformFinished(forService: Service) {
    forService.startForeground(notificationId, notificationBuilder.run {
      setContentText(context.getString(R.string.countdown_finished_notification))
      build()
    })
  }

  @Keep
  @PaperParcel
  data class InitData(@StringRes val title: Int,
                      val pendingIntentClassName: String,
                      val pendingIntentRequestCode: Int,
                      val notificationId: Int,
                      @DrawableRes val largeIconDrawable: Int = R.drawable.icon_rest_notification,
                      @DrawableRes val smallIconDrawable: Int = R.drawable.ic_small_rest_notification) : Parcelable {
    companion object {
      @JvmField val CREATOR = PaperParcelCountDownNotification_InitData.CREATOR
    }

    override fun writeToParcel(dest: android.os.Parcel, flags: Int) = PaperParcelCountDownNotification_InitData.writeToParcel(this, dest, flags)
    override fun describeContents(): Int = 0
  }
}