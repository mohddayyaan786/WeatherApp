package com.weatherapp.util.extension

import android.widget.TextView
import com.weatherapp.util.TIME_FORMAT
import com.weatherapp.util.TIME_FORMAT2
import java.time.Instant
import java.time.ZoneId

fun TextView.milliesToDate(millies: Long) {
    val localTime = millies.let {
        Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalTime()
    }
    this.text = localTime.toString().changeOldTimeFormatToNewTimeFormat(TIME_FORMAT, TIME_FORMAT2)
}

