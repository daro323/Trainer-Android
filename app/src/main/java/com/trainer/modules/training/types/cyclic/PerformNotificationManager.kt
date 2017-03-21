package com.trainer.modules.training.types.cyclic

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import com.trainer.R
import com.trainer.base.BaseNotificationManager
import com.trainer.d2.qualifier.ForApplication
import com.trainer.d2.scope.ApplicationScope
import com.trainer.ui.training.SerieActivity
import javax.inject.Inject

/**
 * Created by dariusz on 21/03/17.
 */
@ApplicationScope
class PerformNotificationManager @Inject constructor(val notificationManager: NotificationManager,
                                                     @ForApplication val context: Context) : BaseNotificationManager() {

  companion object {
    const private val NOTIFICATION_ID = 998
    const private val DUMMY_REQUESTCODE = 222
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

  /**
   * Shown when whole Cycle is complete
   */
  fun showForPerformFinished(forService: Service) {
    forService.startForeground(NOTIFICATION_ID, notificationBuilder.run {
      setContentTitle(context.getString(R.string.cycle_finished_notification_title))
      setContentText("")
      build()
    })
  }

  override fun provideNotificationManager() = notificationManager
  override fun provideContext() = context
  override fun provideContentIntent() = Intent(context, SerieActivity::class.java)
      .run { PendingIntent.getActivity(context, DUMMY_REQUESTCODE, this, PendingIntent.FLAG_UPDATE_CURRENT) }
  override fun provideNotificationId() = NOTIFICATION_ID
}