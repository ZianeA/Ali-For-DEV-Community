package com.aliziane.alifordevcommunity.common

import androidx.annotation.StringRes

sealed class UiResult<T> {
    class Loading<T> : UiResult<T>()
    class Success<T>(val data: T) : UiResult<T>()
    class Error<T>(@StringRes val error: Int) : UiResult<T>()
}