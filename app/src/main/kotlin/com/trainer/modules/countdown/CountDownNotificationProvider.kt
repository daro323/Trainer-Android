package com.trainer.modules.countdown

import android.app.Service
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.countdown.CountDownNotificationProvider.Type.PERFORMING
import com.trainer.modules.countdown.CountDownNotificationProvider.Type.RESTING
import com.trainer.modules.training.rest.RestNotificationManager
import com.trainer.modules.training.types.cyclic.PerformNotificationManager
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by dariusz on 29.03.17.
 */
@ApplicationScope
class CountDownNotificationProvider @Inject constructor(val restingNotificationProvider: Provider<RestNotificationManager>,
                                                        val performingNotificationProvider: Provider<PerformNotificationManager>) {
  interface CountDownNotification {
    fun showNotification(restTime: Int, forService: Service)
    fun hideNotification()
  }

  enum class Type {
    RESTING,
    PERFORMING
  }

  fun getNotification(forType: Type): CountDownNotification = when (forType) {
    RESTING -> restingNotificationProvider.get()
    PERFORMING -> performingNotificationProvider.get()
    else -> throw IllegalArgumentException("Unsupported CountDownNotification Type= $forType")
  }
}