package com.aliziane.alifordevcommunity.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliziane.alifordevcommunity.*
import com.aliziane.alifordevcommunity.common.DEFAULT_STARTED
import com.aliziane.alifordevcommunity.common.DevApi
import com.aliziane.alifordevcommunity.common.UiResult
import com.aliziane.alifordevcommunity.common.runAndCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val devApi: DevApi) : ViewModel() {
    val articles = flow<UiResult<List<Article>>> {
        runAndCatch { devApi.getArticles() }
            .onSuccess { emit(UiResult.Success(it)) }
            .onFailure {
                Timber.e(it)
                emit(UiResult.Error(R.string.error_generic))
            }
    }
        .stateIn(viewModelScope, DEFAULT_STARTED, UiResult.Loading())
}