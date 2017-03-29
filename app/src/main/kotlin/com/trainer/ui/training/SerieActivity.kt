package com.trainer.ui.training

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.base.OnBackSupportingFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.core.training.business.WorkoutPresenter
import com.trainer.core.training.model.Serie
import com.trainer.core.training.model.WorkoutEvent
import com.trainer.core.training.model.WorkoutEvent.SERIE_COMPLETED
import com.trainer.core.training.model.WorkoutEvent.WORKOUT_COMPLETED
import com.trainer.extensions.ioMain
import com.trainer.extensions.setupReplaceFragment
import com.trainer.extensions.with
import com.trainer.modules.training.types.cyclic.Cycle
import com.trainer.modules.training.types.standard.Set
import com.trainer.modules.training.types.standard.SuperSet
import com.trainer.ui.training.cyclic.CycleFragment
import com.trainer.ui.training.standard.SetFragment
import com.trainer.ui.training.standard.SetFragment.Companion.ARG_SET_ID
import com.trainer.ui.training.standard.SetFragment.Companion.ARG_SHOWN_AS_SET_SERIE
import com.trainer.ui.training.standard.SuperSetFragment
import io.reactivex.disposables.Disposables
import javax.inject.Inject


class SerieActivity : BaseActivity(R.layout.activity_serie) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private var workoutEventsSubscription = Disposables.disposed()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    showSerie(presenter.getCurrentSerie())
    subscribeForWorkoutEvents()
  }

  override fun onStop() {
    workoutEventsSubscription.dispose()
    super.onStop()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.serie_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.skip_serie -> {
        presenter.skipSerie()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun subscribeForWorkoutEvents() {
    workoutEventsSubscription.dispose()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .ioMain()
        .subscribe { handleWorkoutEvent(it) }
  }

  private fun handleWorkoutEvent(workoutEvent: WorkoutEvent) {
    when(workoutEvent) {
      SERIE_COMPLETED -> {
        presenter.serieCompleteHandled()
        finish()
      }
      WORKOUT_COMPLETED -> finish()
      else -> {}  // Ignore
    }
  }

  override fun onBackPressed() {
    val fragment = supportFragmentManager.findFragmentById(R.id.serieContainer)
    if (fragment is OnBackSupportingFragment) {
      if (fragment.onBackPressed().not()) {
        super.onBackPressed()
      }
    } else {
      super.onBackPressed()
    }
  }

  private fun showSerie(serie: Serie) {
    when(serie) {
      is Set -> { showSerieAsSet(serie) }
      is SuperSet -> { showSerieAsSuperSet() }
      is Cycle -> { showSerieAsCycle() }
      else -> throw UnsupportedOperationException("Can't show Serie for unsupported type= ${serie.javaClass}")
    }
  }

  private fun showSerieAsSet(set: Set) {
    title = getString(R.string.set)
    setupReplaceFragment(R.id.serieContainer) { SetFragment()
        .with(
            ARG_SET_ID to set.id(),
            ARG_SHOWN_AS_SET_SERIE to true) }
  }

  private fun showSerieAsSuperSet() {
    title = getString(R.string.super_set)
    setupReplaceFragment(R.id.serieContainer) { SuperSetFragment() }
  }

  private fun showSerieAsCycle() {
    title = getString(R.string.cycle)
    setupReplaceFragment(R.id.serieContainer) { CycleFragment() }
  }
}
