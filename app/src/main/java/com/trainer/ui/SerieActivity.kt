package com.trainer.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.widget.FrameLayout
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.extensions.startForResult
import com.trainer.extensions.with
import com.trainer.modules.training.Series
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutEvent
import com.trainer.modules.training.WorkoutEvent.*
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.ui.RestActivity.Companion.EXTRA_REST_TIME_SEC
import com.trainer.ui.SetFragment.Companion.SET_ID
import com.trainer.utils.bindView
import rx.subscriptions.Subscriptions
import javax.inject.Inject



class SerieActivity : BaseActivity(R.layout.activity_set_pager) {

  companion object {
    private val REST_REQUEST_CODE = 666
  }

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private lateinit var adapter: SuperSetPagerAdapter
  private var workoutEventsSubscription = Subscriptions.unsubscribed()

  private val superSetPager: ViewPager by bindView(R.id.pager_view)
  private val setContainer: FrameLayout by bindView(R.id.container)

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
    workoutEventsSubscription.unsubscribe()
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
    workoutEventsSubscription.unsubscribe()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .ioMain()
        .subscribe { handleWorkoutEvent(it) }
  }

  private fun handleWorkoutEvent(workoutEvent: WorkoutEvent) {
    when(workoutEvent) {
      REST -> startForResult<RestActivity>(REST_REQUEST_CODE) { putExtra(EXTRA_REST_TIME_SEC, presenter.getRestTime()) }
      DO_NEXT_SET -> goToNextSet()
      SERIE_COMPLETED -> finish()
      WORKOUT_COMPLETED -> NotImplementedError("WORKOUT_COMPLETED support todo")  // TODO
    }
  }

  private fun goToNextSet() {
    val currentSerie = presenter.getCurrentSerie()
    require(currentSerie is SuperSet) { "goToNextSet can only be done in the context of a SuperSet" }

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
    superSetPager.visibility = GONE
    supportFragmentManager.beginTransaction().apply {
      add(R.id.container, SetFragment().with(SET_ID to set.id()))
    }.commit()
  }

  private fun showSerieAsSuperSet(superSet: SuperSet) {
    setContainer.visibility = GONE
    adapter = SuperSetPagerAdapter(supportFragmentManager, superSet.setList)
    superSetPager.adapter = adapter
  }
}