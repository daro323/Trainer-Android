package com.trainer.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.trainer.R
import com.trainer.base.BaseActivity
import com.trainer.extensions.startActivity
import com.trainer.modules.training.Series
import com.trainer.modules.training.Series.Set
import com.trainer.modules.training.Series.SuperSet
import com.trainer.modules.training.TrainingManager
import com.trainer.ui.model.SetItem
import com.trainer.ui.model.SuperSetItem
import com.trainer.ui.model.SuperSetItemHolder
import com.trainer.utils.bindView
import com.trainer.utils.typedviewholder.TypedViewHolderAdapter
import java.util.*
import javax.inject.Inject

/**
 * Displays workout plan for Given Training Category
 */
class WorkoutListActivity : BaseActivity(R.layout.activity_list) {

  @Inject lateinit var trainingManager: TrainingManager

  private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
  private val typedAdapter: TypedViewHolderAdapter<Any> by lazy {
    TypedViewHolderAdapter.Builder<Any>()
        .addFactory(SuperSetItemHolder.factory{ openSerie(typedAdapter.data.indexOf(it)) })
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

    trainingManager.getCurrentWorkoutPresenter()?.apply {
      title = String.format(getString(R.string.workout), this.getWorkoutTitle())
      showWorkoutList(this.getWorkoutList())
    }
  }

  override fun onBackPressed() {
    if(trainingManager.isWorkoutStarted()) showAlert()
    else super.onBackPressed()
  }

  private fun showWorkoutList(list: List<Series>) {
    val result = ArrayList<Any>(list.size)

    list.forEach { serie ->
      when(serie) {
        is SuperSet -> result.add(createSuperSetItem(serie))
        is Set -> result.add(SetItem(serie.exercise.imageRes, serie.exercise.name))
      }
    }

    typedAdapter.data = result
  }

  private fun createSuperSetItem(superSet: SuperSet): SuperSetItem {
    val imageResList = ArrayList<Int>(superSet.seriesList.size)
    val namesList = ArrayList<String>(superSet.seriesList.size)

    superSet.seriesList
        .flatMap { series -> listOf((series as Set).exercise) }
        .forEach { exercise ->
          imageResList.add(exercise.imageRes)
          namesList.add(exercise.name)
        }
    return SuperSetItem(imageResList, namesList)
  }

  private fun showAlert() {
    // TODO
    trainingManager.abortWorkout()
  }

  private fun openSerie(index: Int) {
    trainingManager.getCurrentWorkoutPresenter()?.apply {
      selectSerie(index)
      startActivity<SetActivity>()
    }
  }
}
