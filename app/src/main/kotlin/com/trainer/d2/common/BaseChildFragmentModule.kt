package com.training.d2.common

import dagger.Module

/**
 * Provides the base child fragment dependencies.
 * The module of the subclasses of the BaseChildFragment are required to include the BaseChildFragmentModule and provide a concrete implementation of the Fragment named CHILD_FRAGMENT
 *
 * Created by dariusz on 11.09.17.
 */
@Module
abstract class BaseChildFragmentModule {


}