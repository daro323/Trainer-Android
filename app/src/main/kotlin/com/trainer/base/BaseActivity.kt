package com.trainer.base

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.angads25.filepicker.model.DialogConfigs.FILE_SELECT
import com.github.angads25.filepicker.model.DialogConfigs.SINGLE_MODE
import com.github.angads25.filepicker.model.DialogProperties
import com.github.angads25.filepicker.view.FilePickerDialog
import com.trainer.R
import com.trainer.TrainingApplication
import com.trainer.d2.common.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.Disposables
import java.io.File
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
abstract class BaseActivity(@LayoutRes private val layoutRes: Int = -1) : AppCompatActivity(), LifecycleRegistryOwner, HasSupportFragmentInjector {

  @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
  private var showDialogSubscription = Disposables.disposed()
  private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    if (layoutRes != -1) setContentView(layoutRes)
  }

  override fun getLifecycle() = lifecycleRegistry

  override fun supportFragmentInjector() = fragmentInjector

  protected fun <T : ViewModel> getViewModel(clazz: Class<T>) = ViewModelProviders.of(this, ViewModelFactory(application as TrainingApplication)).get(clazz)

  protected fun <T> observeLiveData(data: LiveData<T>, action: (T?) -> Unit) {
    data.observe(this, Observer<T>(action))
  }

  protected fun showRetryPopupAlert(@StringRes messageId: Int, retryAction: () -> Unit, cancelAction: () -> Unit) {
    showConfigurablePopupAlert(R.string.retry, R.string.cancel, messageId, retryAction, cancelAction)
  }

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
        .setPositiveButton(yesLabel, { dialog, _ ->
          yesAction()
          dialog.dismiss()
        })
        .setNegativeButton(noLabel, { dialog, _ ->
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
        .setPositiveButton(R.string.ok, { dialog, _ ->
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
        .setPositiveButton(R.string.ok, { dialog, _ ->
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