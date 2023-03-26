package com.weatherapp.util.extension

import android.util.Log
import com.weatherapp.util.LOG_TAG
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.loge(TAG: String = LOG_TAG) = Log.e(TAG, this)
fun String.changeOldTimeFormatToNewTimeFormat(old: String, new: String): String {
    return try {
        val dateFormat = SimpleDateFormat(new, Locale.getDefault())
        val formatter = SimpleDateFormat(old, Locale.getDefault())
        dateFormat.format(formatter.parse(this)!!).toString()
    } catch (ex: ParseException) {
        this
    }
}




