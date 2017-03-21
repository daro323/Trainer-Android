package com.trainer.base

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.trainer.R

/**
 * Created by dariusz on 21/03/17.
 */
abstract class BaseNotificationManager {

  protected val notificationBuilder: NotificationCompat.Builder by lazy { setupNotificationBuilder() }

  fun hideNotification() {
    provideNotificationManager().cancel(provideNotificationId())
  }

  private fun setupNotificationBuilder(): NotificationCompat.Builder {
    val largeIcon = BitmapFactory.decodeResource(provideContext().resources, R.drawable.icon_rest_notification)

    return NotificationCompat.Builder(provideContext())
        .setLargeIcon(largeIcon)
        .setContentIntent(provideContentIntent())
        .setSmallIcon(R.drawable.ic_small_rest_notification)
        .setAutoCancel(false)
  }

  abstract fun provideNotificationManager(): NotificationManager
  abstract fun provideContext(): Context
  abstract fun provideContentIntent(): PendingIntent
  abstract fun provideNotificationId(): Int
}