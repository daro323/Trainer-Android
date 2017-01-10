package com.trainer.extensions

import android.content.SharedPreferences

/**
 * Created by dariusz on 05/01/17.
 */

fun SharedPreferences.saveString(key: String, value: String) {
  edit()
      .putString(key, value)
      .apply()
}

inline fun <S, T: S> Iterable<T>.reduceWithDefault(default: S, firstOperation: (S) -> S, operation: (S, T) -> S): S {
  val iterator = this.iterator()
  if (!iterator.hasNext()) return default
  var accumulator: S = firstOperation(iterator.next())

  while (iterator.hasNext()) {
    accumulator = operation(accumulator, iterator.next())
  }
  return accumulator
}