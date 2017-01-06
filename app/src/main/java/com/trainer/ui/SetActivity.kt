package com.trainer.ui

import android.os.Bundle
import com.trainer.R
import com.trainer.base.BaseActivity

class SetActivity : BaseActivity(R.layout.activity_set) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }
}
