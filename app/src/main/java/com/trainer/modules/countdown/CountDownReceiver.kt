package com.trainer.modules.countdown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.trainer.d2.qualifier.ForApplication

/**
 * Created by dariusz on 23/03/2017.
 */
class CountDownReceiver constructor(private val onCountDownAction: (Int) -> Unit) {

  private val receiver: BroadcastReceiver by lazy {
    object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        intent.getIntExtra(CountDownService.COUNT_DOWN_EVENT_ARG, -1)
            .run {
              require(this != -1) { "CountDownService.COUNT_DOWN_EVENT_ARG received without count down value being provided!" }
              onCountDownAction(this)
            }
      }
    }
  }

  fun register(@ForApplication context: Context) {
    LocalBroadcastManager.getInstance(context).registerReceiver(receiver, IntentFilter(CountDownService.COUNT_DOWN_EVENT_ACTION))
  }

  fun unregisterAndDispose(@ForApplication context: Context) {
    LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
  }
}