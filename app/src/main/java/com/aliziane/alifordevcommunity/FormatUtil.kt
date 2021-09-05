package com.aliziane.alifordevcommunity

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs

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

private const val MEGA_SUFFIX = "m"
private const val KILO_SUFFIX = "k"