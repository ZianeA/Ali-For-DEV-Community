package com.aliziane.alifordevcommunity.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import com.google.accompanist.insets.Insets

@Composable
internal fun rememberInsetsPaddingValues(
    insets: Insets,
    additionalPaddingValues: PaddingValues
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    return com.google.accompanist.insets.rememberInsetsPaddingValues(
        insets = insets,
        additionalStart = additionalPaddingValues.calculateStartPadding(layoutDirection),
        additionalEnd = additionalPaddingValues.calculateEndPadding(layoutDirection),
        additionalTop = additionalPaddingValues.calculateTopPadding(),
        additionalBottom = additionalPaddingValues.calculateBottomPadding(),
    )
}

@SuppressLint("ComposableNaming")
@Composable
internal operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
        top = calculateTopPadding() + other.calculateTopPadding(),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
    )
}