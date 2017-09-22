package com.training.d2.common

import dagger.Module

/**
 * Provides the base fragment dependencies.
 * The module of the subclasses of the BaseFragment are required to include the BaseFragmentModule and provide a concrete implementation of the ForFragment qualifier.
 *
 * Created by dariusz on 11.09.17.
 */
@Module
abstract class BaseFragmentModule {


}