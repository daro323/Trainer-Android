package com.trainer.ui.training

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.commons.StyleUtils
import com.trainer.commons.typedviewholder.TypedViewHolderAdapter
import com.trainer.commons.typedviewholder.registerHolder
import com.trainer.extensions.ioMain
import com.trainer.extensions.start
import com.trainer.modules.export.ExportManager
import com.trainer.modules.training.workout.ProgressStatus.STARTED
import com.trainer.modules.training.workout.Serie
import com.trainer.modules.training.workout.WorkoutEvent.WORKOUT_COMPLETED
import com.trainer.modules.training.workout.WorkoutManager
import com.trainer.modules.training.workout.WorkoutPresenter
import com.trainer.ui.training.stretch.StretchActivity
import com.trainer.ui.training.types.model.CycleItem
import com.trainer.ui.training.types.model.SetItem
import com.trainer.ui.training.types.model.SuperSetItem
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.item_cycle.view.*
import kotlinx.android.synthetic.main.item_set.*
import kotlinx.android.synthetic.main.item_set.view.*
import kotlinx.android.synthetic.main.item_super_set.*
import kotlinx.android.synthetic.main.item_super_set_container.view.*
import javax.inject.Inject

/**
 * Displays workout plan for Given Training Category
 */
class WorkoutListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var workoutManager: WorkoutManager
  @Inject lateinit var exportManager: ExportManager
  private val presenter: WorkoutPresenter by lazy { workoutManager.workoutPresenter ?: throw IllegalStateException("Current workout not set!") }  // can call this only after component.inject()!
  private var workoutEventsSubscription = Disposables.disposed()

  private val typedAdapter = TypedViewHolderAdapter.Builder<Any>().apply {
    registerHolder(R.layout.item_super_set_container) { model: SuperSetItem ->
      itemView.apply {
        superSetItemContainer.setOnClickListener { openSerie(model.id) }
        require(model.imageResList.size == model.namesList.size) { "Super set adapter item invalid - list of images is not the same size as list of names!" }
        model.imageResList.forEachIndexed { i, imageRes ->
          superSetItemContainer.addView(createSetView(LayoutInflater.from(context), imageRes, model.namesList[i], i == model.imageResList.lastIndex, superSetItemContainer))
        }
        superSetItemContainer.setBackgroundColor(ContextCompat.getColor(context, StyleUtils.getColorRes(model.status)))
      }
    }

    registerHolder(R.layout.item_set) { model: SetItem ->
      itemView.apply {
        setItemContentContainer.setOnClickListener { openSerie(model.id) }

        setItemName.text = model.name
        setItemImage.setImageResource(model.imageRes)
        setItemContentContainer.setBackgroundColor(ContextCompat.getColor(context, StyleUtils.getColorRes(model.status)))
      }
    }

    registerHolder(R.layout.item_cycle) { model: CycleItem ->
      itemView.apply {
        cycleItemContainer.setOnClickListener { openSerie(model.id) }

        cycleItemName.text = model.name
        cycleItemContainer.setBackgroundColor(ContextCompat.getColor(context, StyleUtils.getColorRes(model.status)))
      }
    }
  }.build()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    ui_plans_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    ui_plans_list.adapter = typedAdapter
    title = String.format(getString(R.string.workout), presenter.getWorkoutTitle())
    showWorkoutList(presenter.getWorkoutList())
    subscribeForWorkoutEvents()
  }

  override fun onStop() {
    workoutEventsSubscription.dispose()
    super.onStop()
  }

  override fun onBackPressed() {
    if (presenter.getWorkoutStatus() == STARTED) {
      showCancelablePopupAlert(R.string.confirm_workout_abort, {
        workoutManager.abortWorkout()
        finish()
      })
    } else {
      workoutManager.abortWorkout()
      super.onBackPressed()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.workout_menu, menu)

    // Invalidate menu items
    if (workoutManager.hasStretchPlan().not()) menu.removeItem(R.id.show_stretching)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.show_stretching -> {
        StretchActivity.start(presenter.getWorkoutCategory(), this)
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun subscribeForWorkoutEvents() {
    workoutEventsSubscription.dispose()
    workoutEventsSubscription = presenter.onWorkoutEvent()
        .filter { it == WORKOUT_COMPLETED }
        .ioMain()
        .subscribe {
          if (workoutManager.hasStretchPlan()) {
            showConfigurablePopupAlert(R.string.close, R.string.stretch, R.string.workout_complete,
                { completeAndFinish() },
                {
                  StretchActivity.start(presenter.getWorkoutCategory(), this)
                  completeAndFinish()
                })
          } else {
            showConfirmPopupAlert(R.string.workout_complete) { completeAndFinish() }
          }
        }
  }

  private fun completeAndFinish() {
    workoutManager.completeWorkout()
    exportManager.exportCurrentTrainingPlan().ioMain().subscribe()
    finish()
  }

  private fun showWorkoutList(list: List<Serie>) {
    // TODO Refactor
    /*val result = ArrayList<Any>(list.size)

    list.forEach { serie ->
      when (serie) {
        is SuperSet -> result.add(createSuperSetItem(serie))
        is Set -> result.add(SetItem(serie.id(), Exercise.getImageResource(), serie.exercise.name, serie.status()))
        is Cycle -> result.add(CycleItem(serie.id(), serie.name, serie.status()))
        else -> throw IllegalArgumentException("Unsupported serie type= ${serie.javaClass.name}")
      }
    }
    typedAdapter.data = result*/
  }

    // TODO Refactor
  /*private fun createSuperSetItem(superSet: SuperSet): SuperSetItem {
    val imageResList = ArrayList<Int>(superSet.seriesList.size)
    val namesList = ArrayList<String>(superSet.seriesList.size)

    superSet.seriesList
        .flatMap { series -> listOf((series).exercise) }
        .forEach { exercise ->
          imageResList.add(exercise.imageResource)
          namesList.add(exercise.name)
        }
    return SuperSetItem(superSet.id(), imageResList, namesList, superSet.status())
  }*/

  private fun openSerie(serieId: String) {
    presenter.selectSerie(serieId)
    start<SerieActivity>()
  }

  private fun createSetView(inflater: LayoutInflater, @DrawableRes setImageRes: Int, setName: String, isLast: Boolean, container: ViewGroup): View {
    if (isLast) {
      val view = inflater.inflate(R.layout.item_set, container, false)
      setItemImage.setImageResource(setImageRes)
      setItemName.text = setName
      return view
    } else {
      val view = inflater.inflate(R.layout.item_super_set, container, false)
      superSetItemImage.setImageResource(setImageRes)
      superSetItemName.text = setName
      return view
    }
  }
}
