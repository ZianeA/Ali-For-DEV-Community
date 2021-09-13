package com.aliziane.alifordevcommunity.articledetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
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
class ArticleDetailViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val devApi: DevApi
) : ViewModel() {
    val article = stateHandle.getLiveData<Long>(KEY_ARTICLE_ID)
        .asFlow()
        .transform<Long, UiResult<ArticleDetail>> { id ->
            runAndCatch { devApi.getArticleById(id) }
                .onSuccess { article ->
                    val formatted = article.copy(
                        bodyMarkdown = article.bodyMarkdown.substringAfter(MARKDOWN_START_DELIMITER)
                    )
                    emit(UiResult.Success(formatted))
                }
                .onFailure {
                    Timber.e(it)
                    emit(UiResult.Error(R.string.error_generic))
                }
        }
        .stateIn(viewModelScope, DEFAULT_STARTED, UiResult.Loading())

    companion object {
        const val KEY_ARTICLE_ID = "articleId"
        private const val MARKDOWN_START_DELIMITER = "\n---"
    }
}