package com.aliziane.alifordevcommunity.home

import android.content.res.Configuration
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.*
import com.aliziane.alifordevcommunity.common.toPrettyCount
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import timber.log.Timber

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, onClick: (articleId: Long) -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        val articlesResult by homeViewModel.articles.collectAsState()

        when (val result = articlesResult) {
            is UiResult.Error -> {
                /*TODO()*/
            }
            is UiResult.Loading -> {
                /*TODO()*/
            }
            is UiResult.Success -> {
                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    itemsIndexed(result.data) { index, article ->
                        if (index > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Article(article = article, onClick = { onClick(article.id) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun Article(modifier: Modifier = Modifier, article: Article, onClick: () -> Unit = {}) {
    val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.18f)
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.medium
            )
            .clickable(onClick = onClick)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                // A fix for image not loading when height is unbounded.
                // See https://issuetracker.google.com/issues/186012457
                .heightIn(min = 1.dp),
            painter = rememberImagePainter(article.coverImageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_image)
            },
            contentDescription = "Article cover",
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.18f)),
                    painter = rememberImagePainter(article.author.avatarUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_person)
                    },
                    contentDescription = "Author Avatar",
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = article.author.name, style = MaterialTheme.typography.subtitle2)
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = DateUtils.getRelativeTimeSpanString(article.publishedAt.time)
                                .toString(),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = article.title,
                style = MaterialTheme.typography.h6
            )

            Row {
                for ((index, tag) in article.tags.withIndex()) {
                    if (index > 0) {
                        Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                    }
                    Tag(text = tag) {/*TODO*/ }
                }
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Reactions"
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = article.reactionCount.toPrettyCount()
                    )
                    Icon(
                        modifier = Modifier.padding(start = 16.dp),
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments"
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = article.commentCount.toPrettyCount()
                    )
                }
                Row(
                    modifier = modifier.weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.read_time, article.readTimeInMinutes))
                    IconButton(modifier = Modifier.padding(start = 8.dp), onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkAdd,
                            contentDescription = "Bookmark"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArticleDarkPreview() {
    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            Article(article = fakeArticle)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleLightPreview() {
    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            Article(article = fakeArticle)
        }
    }
}