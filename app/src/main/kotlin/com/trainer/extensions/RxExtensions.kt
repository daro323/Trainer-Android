package com.trainer.extensions

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by dariusz on 06/01/17.
 */

fun <T> Observable<T>.ioMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.ioMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.ioMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.ioMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.ioMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())