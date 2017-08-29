package com.trainer.base

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.angads25.filepicker.model.DialogConfigs.FILE_SELECT
import com.github.angads25.filepicker.model.DialogConfigs.SINGLE_MODE
import com.github.angads25.filepicker.model.DialogProperties
import com.github.angads25.filepicker.view.FilePickerDialog
import com.trainer.R
import com.trainer.TrainingApplication
import com.trainer.d2.common.ActivityComponent
import com.trainer.d2.common.ViewModelFactory
import io.reactivex.disposables.Disposables
import java.io.File

/**
 * Created by dariusz on 05/01/17.
 */
abstract class BaseActivity(@LayoutRes private val layoutRes: Int = -1) : AppCompatActivity(), LifecycleRegistryOwner {
  val component: ActivityComponent by lazy { (applicationContext as BaseApplication).activityComponent(this) }
  protected var showDialogSubscription = Disposables.disposed()
  private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (layoutRes != -1) setContentView(layoutRes)
  }

  override fun getLifecycle() = lifecycleRegistry

  protected fun <T : ViewModel> getViewModel(clazz: Class<T>) = ViewModelProviders.of(this, ViewModelFactory(application as TrainingApplication)).get(clazz)

  protected fun showCancelablePopupAlert(@StringRes messageId: Int, action: () -> Unit) {
    showConfigurablePopupAlert(R.string.ok, R.string.cancel, messageId, action, {})
  }

  protected fun showConfigurablePopupAlert(@StringRes yesLabel: Int,
                                           @StringRes noLabel: Int,
                                           @StringRes messageId: Int,
                                           yesAction: () -> Unit,
                                           noAction: () -> Unit) {
    showDialogSubscription.dispose()
    val dialog = AlertDialog.Builder(this)
        .setMessage(messageId)
        .setCancelable(true)
        .setPositiveButton(yesLabel, { dialog, which ->
          yesAction()
          dialog.dismiss()
        })
        .setNegativeButton(noLabel, { dialog, which ->
          noAction()
          dialog.dismiss() })
        .create()
    dialog.show()
    showDialogSubscription = Disposables.fromAction { dialog.dismiss() }
  }

  protected fun showConfirmPopupAlert(@StringRes messageId: Int, action: () -> Unit = {}) {
    showDialogSubscription.dispose()
    val dialog = AlertDialog.Builder(this)
        .setMessage(messageId)
        .setCancelable(false)
        .setPositiveButton(R.string.ok, { dialog, which ->
          action()
          dialog.dismiss()
        })
        .create()
    dialog.show()
    showDialogSubscription = Disposables.fromAction { dialog.dismiss() }
  }

  protected fun showConfirmPopupAlert(message: String, action: () -> Unit = {}) {
    showDialogSubscription.dispose()
    val dialog = AlertDialog.Builder(this)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(R.string.ok, { dialog, which ->
          action()
          dialog.dismiss()
        })
        .create()
    dialog.show()
    showDialogSubscription = Disposables.fromAction { dialog.dismiss() }
  }

  protected fun showFilePickerDialog(rootFile: File, action: (filePath: String) -> Unit) {
    showDialogSubscription.dispose()
    val properties = DialogProperties().apply {
      selection_mode = SINGLE_MODE
      selection_type = FILE_SELECT
      root = rootFile
    }

    val dialog = FilePickerDialog(this, properties).apply {
      title = getString(R.string.select_plan_file)
      setDialogSelectionListener { action(it[0]) }
    }
    dialog.show()
    showDialogSubscription = Disposables.fromAction { dialog.dismiss() }
  }
}