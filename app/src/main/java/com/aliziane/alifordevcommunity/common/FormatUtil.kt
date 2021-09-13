package com.aliziane.alifordevcommunity.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

private const val KILO_SUFFIX = "k"
private const val MEGA_SUFFIX = "m"

internal fun Int.toPrettyCount(): String {
    val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
    formatSymbols.decimalSeparator = '.'
    val formatter = DecimalFormat("#.#", formatSymbols)

    return when {
        abs(this / 1000000) >= 1 -> formatter.format(this / 1000000.0) + MEGA_SUFFIX
        abs(this / 1000) >= 1 -> formatter.format(this / 1000.0) + KILO_SUFFIX
        else -> this.toString()
    }
}

private const val DATE_FORMAT = "MMMM d, yyyy"
private val formatter get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

fun Date.format(): String = formatter.format(this)