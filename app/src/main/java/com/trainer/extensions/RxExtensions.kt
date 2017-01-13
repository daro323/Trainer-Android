package com.trainer.extensions

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by dariusz on 06/01/17.
 */

fun <T> Observable<T>.ioMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

inline fun <reified T> singleOnSubscribe(crossinline body: () -> T): Observable<T> {
  return Observable.create<T> { subscriber ->
    try {
      subscriber.onNext(body())
      subscriber.onCompleted()
    } catch(e: Exception) {
      subscriber.onError(e)
    }
  }
}