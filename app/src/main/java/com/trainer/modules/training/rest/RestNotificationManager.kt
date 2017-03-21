package com.trainer.modules.training.rest

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import com.trainer.R
import com.trainer.base.BaseNotificationManager
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.ui.training.RestActivity
import javax.inject.Inject

/**
 * So far
 * Created by dariusz on 20/01/17.
 */
@ApplicationScope
class RestNotificationManager @Inject constructor(val notificationManager: NotificationManager,
                                                  @ForApplication val context: Context) : BaseNotificationManager() {
  companion object {
    const private val NOTIFICATION_ID = 997
    const private val DUMMY_REQUESTCODE = 111
  }

  fun showNotification(forService: Service) {
    forService.startForeground(NOTIFICATION_ID, notificationBuilder.run {
      setContentTitle(context.getString(R.string.rest_ongoing_notification_title))
      build()
    })
  }

  fun updateNotification(restTime: Int, forService: Service) {
    forService.startForeground(NOTIFICATION_ID, notificationBuilder.run {
      setContentText(String.format(context.getString(R.string.notification_countdown_text), restTime))
      build()
    })
  }

  fun showForRestFinished(forService: Service) {
    forService.startForeground(NOTIFICATION_ID, notificationBuilder.run {
      setContentTitle(context.getString(R.string.rest_finished_notification_title))
      setContentText("")
      build()
    })
  }

  override fun provideNotificationManager() = notificationManager
  override fun provideContext() = context
  override fun provideContentIntent() = Intent(context, RestActivity::class.java)
      .run { PendingIntent.getActivity(context, DUMMY_REQUESTCODE, this, PendingIntent.FLAG_UPDATE_CURRENT) }
  override fun provideNotificationId() = NOTIFICATION_ID
}