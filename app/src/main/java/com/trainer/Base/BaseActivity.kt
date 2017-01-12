package com.trainer.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.trainer.R
import com.trainer.d2.common.ActivityComponent
import rx.Subscription
import rx.subscriptions.Subscriptions

/**
 * Created by dariusz on 05/01/17.
 */
abstract class BaseActivity(@LayoutRes private val layoutRes: Int = -1) : AppCompatActivity() {
  val component: ActivityComponent by lazy { (applicationContext as BaseApplication).activityComponent(this) }
  protected var showDialogSubscription: Subscription = Subscriptions.unsubscribed()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (layoutRes != -1) setContentView(layoutRes)
  }

  protected fun showCancelablePopupAlert(@StringRes messageId: Int, action: () -> Unit) {
    showDialogSubscription.unsubscribe()
    val dialog = AlertDialog.Builder(this)
        .setMessage(messageId)
        .setCancelable(true)
        .setPositiveButton(R.string.ok, { dialog, which ->
          action()
          dialog.dismiss()
        })
        .setNegativeButton(R.string.cancel, { dialog, which -> dialog.dismiss() })
        .create()
    dialog.show()
    showDialogSubscription = Subscriptions.create { dialog.dismiss() }
  }

  protected fun showConfirmPopupAlert(@StringRes messageId: Int, action: () -> Unit) {
    showDialogSubscription.unsubscribe()
    val dialog = AlertDialog.Builder(this)
        .setMessage(messageId)
        .setCancelable(false)
        .setPositiveButton(R.string.ok, { dialog, which ->
          action()
          dialog.dismiss()
        })
        .create()
    dialog.show()
    showDialogSubscription = Subscriptions.create { dialog.dismiss() }
  }
}