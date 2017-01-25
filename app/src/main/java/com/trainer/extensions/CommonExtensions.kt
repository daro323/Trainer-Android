package com.trainer.extensions

import android.content.SharedPreferences
import android.util.Log
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset.UTC
import org.threeten.bp.temporal.ChronoUnit
import java.io.File


/**
 * Created by dariusz on 05/01/17.
 */

fun SharedPreferences.saveString(key: String, value: String?) {
  edit()
      .putString(key, value)
      .apply()
}

fun SharedPreferences.saveInt(key: String, value: Int) {
  edit()
      .putInt(key, value)
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

fun File.writeString(data: String) {
  delete()
  createNewFile()
  writeText(data)
  Log.d("writeString", "Successfully written data to= ${absolutePath}")
}

fun LocalDateTime.daysSince(fromDate: LocalDateTime) = ChronoUnit.DAYS.between(fromDate, this)

fun LocalDateTime.toTimestamp() = toInstant(UTC).epochSecond

fun Long.toLocalDateTimeObject() = LocalDateTime.ofEpochSecond(this, 0, UTC)