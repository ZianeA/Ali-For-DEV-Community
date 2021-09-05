package com.aliziane.alifordevcommunity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val devApi: DevApi) : ViewModel() {
    val articles = flow { emit(devApi.getArticles()) }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)
}