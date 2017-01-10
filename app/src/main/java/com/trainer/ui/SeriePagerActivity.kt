package com.trainer.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View.GONE
import android.widget.FrameLayout
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.with
import com.trainer.modules.training.Series
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.workout.WorkoutPresenter
import com.trainer.ui.SetFragment.Companion.SET_ID
import com.trainer.utils.bindView
import javax.inject.Inject

class SeriePagerActivity : BaseActivity(R.layout.activity_set_pager) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // call this after component.inject()
  private lateinit var adapter: SeriePagerAdapter

  private val superSetPager: ViewPager by bindView(R.id.pager_view)
  private val setContainer: FrameLayout by bindView(R.id.container)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    showSerie(presenter.getCurrentSerie())
  }

  private fun showSerie(serie: Series) {
    when(serie) {
      is Set -> { showSet(serie) }
      is SuperSet -> { showSuperSet(serie) }
      else -> throw UnsupportedOperationException("Can't show Serie for unsupported type= ${serie.javaClass}")
    }
  }

  private fun showSet(set: Set) {
    superSetPager.visibility = GONE
    supportFragmentManager.beginTransaction().apply {
      add(R.id.container, SetFragment().with(SET_ID to set.id()))
    }.commit()
  }

  private fun showSuperSet(superSet: SuperSet) {
    setContainer.visibility = GONE
    adapter = SeriePagerAdapter(supportFragmentManager, superSet.setList)
    superSetPager.adapter = adapter
  }
}
