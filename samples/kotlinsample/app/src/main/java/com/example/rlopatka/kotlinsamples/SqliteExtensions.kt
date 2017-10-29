package com.example.rlopatka.kotlinsamples

import android.content.ContentValues
import android.database.Cursor
import java.util.*

/**
 * Created by rlopatka on 29.10.2017.
 */
// cursor extensions
fun Cursor.getBoolean(column: String): Boolean {
    val index = this.getColumnIndexOrThrow(column)

    if (getInt(index) == 0){
        return false
    }

    return true
}

fun Cursor.getUUID(column: String): UUID {
    val index = this.getColumnIndexOrThrow(column)

    return UUID.fromString(this.getString(index))
}

fun Cursor.getInt(column: String): Int {
    val index = this.getColumnIndexOrThrow(column)

    return this.getInt(index)
}

// content values
fun ContentValues.set(column: String, value: Boolean?) {
    if (value == null){
        return
    }

    this.put(column, value)
}

fun ContentValues.set(column: String,value: Int?) {
    if (value == null){
        return
    }

    this.put(column, value)
}

fun ContentValues.set(column: String, value: UUID?) {
    if (value == null){
        return
    }
    this.put(column, value.toString())
}