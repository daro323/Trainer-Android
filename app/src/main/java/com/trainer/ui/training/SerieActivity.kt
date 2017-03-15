package com.trainer.ui.training

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.extensions.setupFragment
import com.trainer.extensions.startForResult
import com.trainer.extensions.with
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.modules.training.coredata.Series
import com.trainer.modules.training.coredata.WorkoutEvent
import com.trainer.modules.training.coredata.WorkoutEvent.*
import com.trainer.modules.training.standard.SuperSet
import com.trainer.modules.training.standard.Set
import com.trainer.ui.training.SetFragment.Companion.ARG_SET_ID
import com.trainer.ui.training.model.SuperSetPagerAdapter
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_set_pager.*
import javax.inject.Inject


class SerieActivity : BaseActivity(R.layout.activity_set_pager) {

  companion object {
    const private val REST_REQUEST_CODE = 666
  }

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private lateinit var adapter: SuperSetPagerAdapter
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

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when(requestCode) {
      REST_REQUEST_CODE -> presenter.restComplete()
    }
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
      REST -> startForResult<RestActivity>(REST_REQUEST_CODE)
      DO_NEXT_SERIE -> goToNextSerie()
      SERIE_COMPLETED -> {
        presenter.serieCompleteHandled()
        finish()
      }
      WORKOUT_COMPLETED -> finish()
    }
  }

  private fun goToNextSerie() {
    val currentSerie = presenter.getCurrentSerie()
    if(currentSerie is Set) return   // Ignore

    val currentSet = presenter.getCurrentSet()
    superSetPager.currentItem = (currentSerie as SuperSet).setList.indexOf(currentSet)
  }

  private fun showSerie(serie: Series) {
    when(serie) {
      is Set -> { showSerieAsSet(serie) }
      is SuperSet -> { showSerieAsSuperSet(serie) }
      else -> throw UnsupportedOperationException("Can't show Serie for unsupported type= ${serie.javaClass}")
    }
  }

  private fun showSerieAsSet(set: Set) {
    title = getString(R.string.set)
    superSetPager.visibility = GONE
    setupFragment(R.id.setContainer) { SetFragment().with(ARG_SET_ID to set.id()) }
  }

  private fun showSerieAsSuperSet(superSet: SuperSet) {
    title = getString(R.string.super_set)
    setContainer.visibility = GONE
    adapter = SuperSetPagerAdapter(supportFragmentManager, superSet.setList)
    superSetPager.adapter = adapter
    goToNextSerie()
  }
}
