package com.aliziane.alifordevcommunity.common

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.CancellationException

private const val DEFAULT_STOP_TIMEOUT_MILLIS = 5000L

/**
 * The recommended started behavior that is similar to LiveData.
 */
val DEFAULT_STARTED = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT_MILLIS)

/**
 * Similar to [runCatching] but rethrows if [CancellationException]
 */
inline fun <R> runAndCatch(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }
}