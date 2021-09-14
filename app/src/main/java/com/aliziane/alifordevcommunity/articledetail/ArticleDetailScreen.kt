package com.aliziane.alifordevcommunity.articledetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.aliziane.alifordevcommunity.common.*
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import java.net.URI

@Composable
fun ArticleDetailScreen(articleDetailViewModel: ArticleDetailViewModel) {
    val articleResult by articleDetailViewModel.article.collectAsState()

    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            when (val result = articleResult) {
                is UiResult.Error<ArticleDetail> -> {
                    /*TODO()*/
                }
                is UiResult.Loading<ArticleDetail> -> {
                    /*TODO()*/
                }
                is UiResult.Success<ArticleDetail> -> {
                    Column {
                        ArticleDetail(modifier = Modifier.weight(1f), article = result.data)
                        Footer()
                    }
                }
            }
        }
    }
}

@Composable
private fun Footer(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), elevation = 8.dp) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            val buttonModifier = Modifier.padding(8.dp)
            IconButton(modifier = buttonModifier, onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Reactions"
                )
            }
            IconButton(modifier = buttonModifier, onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_unicorn),
                    contentDescription = "Unicorn"
                )
            }
            IconButton(modifier = buttonModifier, onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkAdd,
                    contentDescription = "Bookmark"
                )
            }
            IconButton(modifier = buttonModifier, onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More options"
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ArticleDetail(modifier: Modifier = Modifier, article: ArticleDetail) {
    Column(modifier = modifier.verticalScroll(state = rememberScrollState())) {
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

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = article.title,
                Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )

            Row(Modifier.padding(top = 16.dp)) {
                for ((index, tag) in article.tags.withIndex()) {
                    if (index > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Tag(text = tag) {/*TODO*/ }
                }
            }

            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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

                Text(
                    text = article.author.name,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.subtitle2
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.medium,
                    LocalTextStyle provides MaterialTheme.typography.body2
                ) {
                    Text(text = article.publishedAt.format())
                    Text(text = "•", modifier = Modifier.padding(horizontal = 8.dp))
                    Text(text = stringResource(R.string.read_time, article.readTimeInMinutes))
                }
            }

            if (article.url != article.canonicalUrl) {
                val urlHost = URI(article.canonicalUrl).host
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

            SelectionContainer(modifier = Modifier.padding(top = 16.dp)) {
                Column {
                    MaterialRichText {
                        Markdown(checkNotNull(article.bodyMarkdown))
                    }
                }
            }
        }
    }
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

@Preview
@Composable
private fun FooterPreview() {
    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            Footer()
        }
    }
}
