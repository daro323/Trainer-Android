package com.trainer.ui.training.cyclic

import com.trainer.R
import com.trainer.base.BaseFragment
import com.trainer.core.training.model.Exercise
import com.trainer.d2.common.ActivityComponent
import com.trainer.modules.training.cyclic.Cycle
import com.trainer.modules.training.cyclic.CyclicRoutine
import kotlinx.android.synthetic.main.fragment_cycle.*

/**
 * Created by dariusz on 15/03/17.
 */
// TODO: Implement meee
class CycleFragment : BaseFragment(R.layout.fragment_cycle) {
  val dummyCycle = getDummyCycle()

  companion object {
    private fun getDummyCycle() = Cycle(
        "666",
        listOf(
            CyclicRoutine(Exercise("Dummy Exercise"), 30, 15),
            CyclicRoutine(Exercise("Dummy Exercise 2"), 40, 20)),
        60, 5)
  }

  override fun inject(component: ActivityComponent) {
    component.inject(this)
  }

  override fun onStart() {
    super.onStart()
    header.start()
  }
}