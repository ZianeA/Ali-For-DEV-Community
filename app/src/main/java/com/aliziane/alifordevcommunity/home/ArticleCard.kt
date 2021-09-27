package com.aliziane.alifordevcommunity.home

import android.content.res.Configuration
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.aliziane.alifordevcommunity.common.UserAvatar
import com.aliziane.alifordevcommunity.common.Tag
import com.aliziane.alifordevcommunity.common.fakeArticle
import com.aliziane.alifordevcommunity.common.toPrettyCount
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme

@Composable
fun Article(modifier: Modifier = Modifier, article: Article, onClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.18f),
                shape = MaterialTheme.shapes.medium
            )
            .clickable(onClick = onClick)
    ) {
        CoverImage(article.coverImageUrl)

        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(article.author.avatarUrl)

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = article.author.name, style = MaterialTheme.typography.subtitle2)
                    PublishDate(article.publishedAt.time)
                }
            }

            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = article.title,
                style = MaterialTheme.typography.h6
            )

            Tags(article.tags)

            Spacer(modifier = Modifier.height(8.dp))

            Metadata(modifier, article)
        }
    }
}

@Composable
private fun Metadata(modifier: Modifier, article: Article) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
            Text(text = stringResource(R.string.read_time, article.readTimeMinutes))
            OutlinedButton(modifier = Modifier.padding(start = 8.dp), onClick = { /*TODO*/ }) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun Tags(tags: List<String>) {
    Row {
        for ((index, tag) in tags.withIndex()) {
            if (index > 0) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Tag(text = tag) {/*TODO*/ }
        }
    }
}

@Composable
private fun PublishDate(time: Long) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = DateUtils.getRelativeTimeSpanString(time).toString(),
            style = MaterialTheme.typography.body2
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun CoverImage(coverImageUrl: String?) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            // A fix for image not loading when height is unbounded.
            // See https://issuetracker.google.com/issues/186012457
            .heightIn(min = 1.dp),
        painter = rememberImagePainter(coverImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_image)
        },
        contentDescription = "Article cover",
        contentScale = ContentScale.FillWidth
    )
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