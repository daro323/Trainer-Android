package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.ioMain
import com.trainer.extensions.start
import com.trainer.modules.training.ProgressStatus.STARTED
import com.trainer.modules.training.Series
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.TrainingManager
import com.trainer.modules.training.WorkoutEvent.WORKOUT_COMPLETED
import com.trainer.modules.training.WorkoutPresenter
import com.trainer.ui.model.SetItem
import com.trainer.ui.model.SetItemHolder
import com.trainer.ui.model.SuperSetItem
import com.trainer.ui.model.SuperSetItemHolder
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolderAdapter
import rx.subscriptions.Subscriptions
import java.util.*
import javax.inject.Inject

/**
 * Displays workout plan for Given Training Category
 */
class WorkoutListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var trainingManager: TrainingManager
  private val presenter: WorkoutPresenter by lazy { trainingManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // can call this only after component.inject()!
  private var workoutEventsSubscription = Subscriptions.unsubscribed()

  private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
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
    require(trainingManager.isWorkoutActive()) { "WorkoutListActivity was shown for non active workout!" }
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

  private fun subscribeForWorkoutEvents() {
    workoutEventsSubscription.unsubscribe()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .filter { it == WORKOUT_COMPLETED }
        .ioMain()
        .subscribe {
          trainingManager.completeWorkout()
          showConfirmPopupAlert(R.string.workout_complete, { finish() })
        }
  }

  private fun showWorkoutList(list: List<Series>) {
    val result = ArrayList<Any>(list.size)

    list.forEach { serie ->
      when (serie) {
        is SuperSet -> result.add(createSuperSetItem(serie))
        is Set -> result.add(SetItem(serie.exercise.imageRes, serie.exercise.name, serie.getStatus()))
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
          imageResList.add(exercise.imageRes)
          namesList.add(exercise.name)
        }
    return SuperSetItem(imageResList, namesList, superSet.getStatus())
  }

  private fun openSerie(index: Int) {
    presenter.selectSerie(index)
    start<SerieActivity>()
  }
}
