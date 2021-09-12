package com.aliziane.alifordevcommunity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val devApi: DevApi
) : ViewModel() {
    val article = stateHandle.getLiveData<Long>(KEY_ARTICLE_ID)
        .asFlow()
        .transform<Long, UiResult<ArticleDetail>> { id ->
            runAndCatch { devApi.getArticleById(id) }
                .onSuccess { emit(UiResult.Success(it)) }
                .onFailure { emit(UiResult.Error(R.string.error_generic)) }
        }
        .stateIn(viewModelScope, DEFAULT_STARTED, UiResult.Loading())

    companion object {
        const val KEY_ARTICLE_ID = "articleId"
    }
}