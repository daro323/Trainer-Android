package com.trainer.modules.init

import android.util.Log
import com.trainer.d2.scope.ApplicationScope
import com.trainer.modules.training.TrainingRepository
import javax.inject.Inject

/**
 * Created by dariusz on 05/01/17.
 */
@ApplicationScope
class TrainingDataInitializer @Inject constructor(val trainingRepo: TrainingRepository) {

  init {
    initChest()
    initBack()
    initShoulders()
    initArms()
    initLegs()
  }

  private fun initChest() {
    Log.d("INIT", "initChest")
  }

  private fun initBack() {
    Log.d("INIT", "initBack")

  }

  private fun initShoulders() {
    Log.d("INIT", "initShoulders")

  }

  private fun initArms() {
    Log.d("INIT", "initArms")

  }

  private fun initLegs() {
    Log.d("INIT", "initLegs")

  }
}