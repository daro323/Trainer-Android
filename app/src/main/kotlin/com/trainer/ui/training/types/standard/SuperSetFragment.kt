package com.trainer.ui.training.types.standard

import android.os.Bundle
import android.view.View
import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.core.training.business.TrainingManager
import com.trainer.d2.common.ActivityComponent
import com.trainer.extensions.ioMain
import com.trainer.modules.training.types.standard.StandardPresenterHelper
import com.trainer.modules.training.types.standard.StandardStateEvent
import com.trainer.modules.training.types.standard.SuperSet
import com.trainer.ui.training.types.model.SuperSetPagerAdapter
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.fragment_pager.*
import javax.inject.Inject

/**
 * Created by dariusz on 15/03/17.
 */
class SuperSetFragment : BaseFragment(R.layout.fragment_pager) {

  @Inject lateinit var trainingManager: TrainingManager
  private lateinit var adapter: SuperSetPagerAdapter
  private lateinit var superSet: SuperSet
  private var standardEventsSubscription = Disposables.disposed()
  private val presenterHelper: StandardPresenterHelper by lazy { (trainingManager.workoutPresenter?.getHelper() ?: throw IllegalStateException("Current workout presenter not set!") ) as StandardPresenterHelper }  // can call this only after component.inject()!

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initialize()
  }

  override fun onStart() {
    super.onStart()
    subscribeForStandardEvents()
  }

  override fun onStop() {
    standardEventsSubscription.dispose()
    super.onStop()
  }

  private fun initialize() {
    superSet = presenterHelper.getSerie() as SuperSet
    adapter = SuperSetPagerAdapter(childFragmentManager, superSet.seriesList)
    pagerView.adapter = adapter
    goToNextSet()
  }

  private fun subscribeForStandardEvents() {
    standardEventsSubscription.dispose()
    standardEventsSubscription = presenterHelper.getStandardStateEvents()
        .ioMain()
        .filter { it == StandardStateEvent.DO_NEXT }
        .subscribe { goToNextSet() }
  }

  private fun goToNextSet() {
    val currentSet = presenterHelper.getCurrentSet()
    pagerView.currentItem = superSet.seriesList.indexOf(currentSet)
  }
}