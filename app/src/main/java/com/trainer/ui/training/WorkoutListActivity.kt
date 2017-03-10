package com.trainer.ui.training

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.extensions.start
import com.trainer.modules.export.ExportManager
import com.trainer.modules.training.ProgressStatus.STARTED
import com.trainer.modules.training.Series
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutEvent.WORKOUT_COMPLETED
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.ui.training.model.SetItem
import com.trainer.ui.training.model.SetItemHolder
import com.trainer.ui.training.model.SuperSetItem
import com.trainer.ui.training.model.SuperSetItemHolder
import com.trainer.commons.typedviewholder.TypedViewHolderAdapter
import kotlinx.android.synthetic.main.activity_list.*
import rx.subscriptions.Subscriptions
import java.util.*
import javax.inject.Inject

/**
 * Displays workout plan for Given Training Category
 */
class WorkoutListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var trainingManager: TrainingManager
  @Inject lateinit var exportManager: ExportManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // can call this only after component.inject()!
  private var workoutEventsSubscription = Subscriptions.unsubscribed()

  private val typedAdapter: TypedViewHolderAdapter<Any> by lazy {
    TypedViewHolderAdapter.Builder<Any>()
        .addFactory(SuperSetItemHolder.factory { openSerie(typedAdapter.data.indexOf(it)) })
        .addFactory(SetItemHolder.factory { openSerie(typedAdapter.data.indexOf(it)) })
        .build()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    recyclerView.adapter = typedAdapter
    title = String.format(getString(R.string.workout), presenter.getWorkoutTitle())
    showWorkoutList(presenter.getWorkoutList())
    subscribeForWorkoutEvents()
  }

  override fun onStop() {
    workoutEventsSubscription.unsubscribe()
    super.onStop()
  }

  override fun onBackPressed() {
    if (presenter.getWorkoutStatus() == STARTED) {
      showCancelablePopupAlert(R.string.confirm_workout_abort, {
        trainingManager.abortWorkout()
        finish()
      })
    } else {
      trainingManager.abortWorkout()
      super.onBackPressed()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.workout_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.show_stretching -> {
        StretchActivity.start(presenter.getWorkoutCategory(), this)
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun subscribeForWorkoutEvents() {
    workoutEventsSubscription.unsubscribe()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .filter { it == WORKOUT_COMPLETED }
        .ioMain()
        .subscribe {
          showConfigurablePopupAlert(R.string.close, R.string.stretch, R.string.workout_complete,
              {
                completeAndFinish()
              },
              {
                StretchActivity.start(presenter.getWorkoutCategory(), this)
                completeAndFinish()
              })
        }
  }

  private fun completeAndFinish() {
    trainingManager.completeWorkout()
    exportManager.exportCurrentTrainingPlan().ioMain().subscribe()
    finish()
  }

  private fun showWorkoutList(list: List<Series>) {
    val result = ArrayList<Any>(list.size)

    list.forEach { serie ->
      when (serie) {
        is SuperSet -> result.add(createSuperSetItem(serie))
        is Set -> result.add(SetItem(serie.exercise.imageResource(), serie.exercise.name, serie.status()))
      }
    }
    typedAdapter.data = result
  }

  private fun createSuperSetItem(superSet: SuperSet): SuperSetItem {
    val imageResList = ArrayList<Int>(superSet.setList.size)
    val namesList = ArrayList<String>(superSet.setList.size)

    superSet.setList
        .flatMap { series -> listOf((series).exercise) }
        .forEach { exercise ->
          imageResList.add(exercise.imageResource())
          namesList.add(exercise.name)
        }
    return SuperSetItem(imageResList, namesList, superSet.status())
  }

  private fun openSerie(index: Int) {
    presenter.selectSerie(index)
    start<SerieActivity>()
  }
}
