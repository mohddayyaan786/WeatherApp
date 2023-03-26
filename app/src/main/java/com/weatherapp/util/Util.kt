package com.weatherapp.util

import java.math.RoundingMode
import java.text.SimpleDateFormat

import java.util.*

fun currentDate(): String = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(Date()).toString()

fun kelvinToCentigrade(t: Double): Double {
    var intTemp = t
    intTemp = intTemp.minus(273)
    return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
}


