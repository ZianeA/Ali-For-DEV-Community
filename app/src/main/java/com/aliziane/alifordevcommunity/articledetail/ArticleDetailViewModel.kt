package com.aliziane.alifordevcommunity.articledetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.aliziane.alifordevcommunity.*
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
    private val _uiState = MutableStateFlow(ArticleDetailUiState())
    val uiState get() = _uiState.asStateFlow()

    private val article = stateHandle.getLiveData<Long>(KEY_ARTICLE_ID)
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

    private val comments = stateHandle.getLiveData<Long>(KEY_ARTICLE_ID)
        .asFlow()
        .transform<Long, UiResult<List<Comment>>> { articleId ->
            runAndCatch { devApi.getComments(articleId) }
                .onSuccess { comments ->
                    Timber.d("Comments before formatting: $comments")
                    val formatted = comments.map { c ->
                        c.copy(bodyHtml = c.bodyHtml.replace(SVG_TAG, ""))
                    }
                    Timber.d("Comments after formatting: $comments")
                    emit(UiResult.Success(formatted))
                }
                .onFailure {
                    Timber.e(it)
                    emit(UiResult.Error(R.string.error_generic))
                }
        }

    init {
        combine(article, comments) { article, comment -> ArticleDetailUiState(article, comment) }
            .onEach { uiState -> _uiState.value = uiState }
            .launchIn(viewModelScope)
    }

    companion object {
        const val KEY_ARTICLE_ID = "articleId"
        private const val MARKDOWN_START_DELIMITER = "\n---"
    }
}

data class ArticleDetailUiState(
    val articleDetail: UiResult<ArticleDetail>? = null,
    val comments: UiResult<List<Comment>>? = null
)

private const val SVG_TAG =
    "\n    <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"20px\" height=\"20px\" viewbox=\"0 0 24 24\" " +
            "class=\"highlight-action crayons-icon highlight-action--fullscreen-on\"><title>Enter " +
            "fullscreen mode</title>\n    <path d=\"M16 3h6v6h-2V5h-4V3zM2 3h6v2H4v4H2V3zm18 " +
            "16v-4h2v6h-6v-2h4zM4 19h4v2H2v-6h2v4z\"></path>\n</svg>\n\n    " +
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"20px\" height=\"20px\" " +
            "viewbox=\"0 0 24 24\" class=\"highlight-action crayons-icon highlight-action--fullscreen-off\">" +
            "<title>Exit fullscreen mode</title>\n    <path d=\"M18 7h4v2h-6V3h2v4zM8 9H2V7h4V3h2v6zm10" +
            " 8v4h-2v-6h6v2h-4zM8 15v6H6v-4H2v-2h6z\"></path>\n</svg>\n\n"