package com.aliziane.alifordevcommunity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val devApi: DevApi) : ViewModel() {
    val articles = flow<UiResult<List<Article>>> {
        runAndCatch { devApi.getArticles() }
            .onSuccess { UiResult.Success(it) }
            .onFailure { UiResult.Error<List<Article>>(R.string.error_generic) }
    }
        .stateIn(viewModelScope, DEFAULT_STARTED, UiResult.Loading())
}