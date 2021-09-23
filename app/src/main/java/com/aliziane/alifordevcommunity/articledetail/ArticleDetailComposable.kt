package com.aliziane.alifordevcommunity.articledetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.Tag
import com.aliziane.alifordevcommunity.common.fakeArticleDetail
import com.aliziane.alifordevcommunity.common.format
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import java.net.URI
import java.util.*

@Composable
fun ArticleDetail(modifier: Modifier = Modifier, article: ArticleDetail) {
    Column(modifier) {
        CoverImage(article.coverImageUrl)

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.title,
                Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )

            Tags(modifier = Modifier.padding(top = 16.dp), tags = article.tags)

            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AuthorAvatar(article.author.avatarUrl)

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = article.author.name, style = MaterialTheme.typography.subtitle2)
                    PublishDate(article.publishedAt, article.readTimeMinutes)
                }
            }

            CanonicalUrl(article.url, article.canonicalUrl)

            Body(article.bodyMarkdown)
        }
    }
}

@Composable
private fun Body(bodyMarkdown: String) {
    SelectionContainer(modifier = Modifier.padding(top = 16.dp)) {
        MaterialRichText {
            Markdown(bodyMarkdown)
        }
    }
}

@Composable
private fun CanonicalUrl(url: String, canonicalUrl: String) {
    if (url != canonicalUrl) {
        val urlHost = URI(canonicalUrl).host
        val annotatedString = buildAnnotatedString {
            pushStyle(
                SpanStyle(
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                    fontStyle = FontStyle.Italic
                )
            )
            append(stringResource(id = R.string.published_originally))
            pushStyle(SpanStyle(Color(0xFF007BFF)))
            append(" $urlHost")
        }

        ClickableText(text = annotatedString, onClick = { /*TODO*/ })
    }
}

@Composable
private fun PublishDate(publishDate: Date, readTimeMinutes: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium,
            LocalTextStyle provides MaterialTheme.typography.body2
        ) {
            Text(text = publishDate.format())
            Text(text = "•", modifier = Modifier.padding(horizontal = 8.dp))
            Text(text = stringResource(R.string.read_time, readTimeMinutes))
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun AuthorAvatar(avatarUrl: String) {
    Image(
        modifier = Modifier
            .size(40.dp)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.18f)),
        painter = rememberImagePainter(avatarUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_person)
        },
        contentDescription = "Author Avatar",
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun Tags(modifier: Modifier, tags: List<String>) {
    Row(modifier) {
        for ((index, tag) in tags.withIndex()) {
            if (index > 0) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Tag(text = tag) {/*TODO*/ }
        }
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

@Preview
@Composable
private fun ArticleDetailPreview() {
    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            ArticleDetail(article = fakeArticleDetail)
        }
    }
}