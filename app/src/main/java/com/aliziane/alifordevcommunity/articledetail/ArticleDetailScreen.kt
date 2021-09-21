package com.aliziane.alifordevcommunity.articledetail

import android.text.format.DateUtils
import android.widget.TextView
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.*
import com.aliziane.alifordevcommunity.common.network.Iso8601Utils
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import kotlinx.coroutines.flow.combine
import java.net.URI

@Composable
fun ArticleDetailScreen(articleDetailViewModel: ArticleDetailViewModel) {
    val uiState by articleDetailViewModel.uiState.collectAsState()

    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    when (val article = uiState.articleDetail) {
                        is UiResult.Error -> {
                            /*TODO()*/
                        }
                        is UiResult.Loading -> {
                            /*TODO()*/
                        }
                        is UiResult.Success -> {
                            item(key = article.data.id) {
                                ArticleDetail(article = article.data)
                            }
                        }
                    }
                    when (val comments = uiState.comments) {
                        is UiResult.Error -> {
                            TODO()
                        }
                        is UiResult.Loading -> {
                            TODO()
                        }
                        is UiResult.Success -> {
                            itemsIndexed(items = comments.data, key = { _, c -> c.id }) { _, c ->
                                Comment(c, Modifier.padding(horizontal = 16.dp))
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }

                Footer()
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
                    contentDescription = "React"
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
    Column(modifier) {
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

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun Comment(comment: Comment, modifier: Modifier = Modifier) {
    Row(modifier) {
        Column(
            modifier = Modifier.padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(32.dp)
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.18f)),
                painter = rememberImagePainter(comment.author.avatarUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_person)
                },
                contentDescription = "Author Avatar",
                contentScale = ContentScale.Crop
            )

            SmallIconButton(
                modifier = Modifier
                    .padding(top = 8.dp),
                onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.UnfoldLess,
                    contentDescription = "Fold comment",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                )
            }
        }

        Column {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.18f),
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, top = 16.dp)
                    ) {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(text = comment.author.name, fontWeight = FontWeight.Medium)
                            Text(text = "•", modifier = Modifier.padding(horizontal = 8.dp))
                            Text(
                                text = DateUtils.getRelativeTimeSpanString(comment.createdAt.time)
                                    .toString()
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.End) {
                        SmallIconButton(
                            modifier = Modifier.padding(4.dp),
                            onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "More options"
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AndroidView(factory = { context ->
                        val textView = TextView(context)
                        val markwon = Markwon.builder(context)
                            .usePlugin(HtmlPlugin.create())
                            .build()
                        markwon.setMarkdown(textView, comment.bodyHtml)
                        textView
                    })
                    /*MaterialRichText {
                        Markdown(checkNotNull(comment.bodyHtml.trim()))
                    }*/
                }
            }

            Row(modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                TextButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Reaction"
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Like"
                    )
                }
                TextButton(modifier = Modifier.padding(start = 8.dp), onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Reply"
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Reply"
                    )
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

@Preview
@Composable
private fun CommentPreview() {
    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            Comment(comment = fakeComment)
        }
    }
}

@Composable
fun SmallIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = RippleRadius)
            )
            .then(IconButtonSizeModifier),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}

private val RippleRadius = 20.dp
private val IconButtonSizeModifier = Modifier.size(40.dp)

private val fakeComment =
    Comment(
        "101",
        Iso8601Utils.parse("2021-09-08T00:00:00Z"),
        "<h3>This is a fake comment!</h3>",
        fakeUser,
        emptyList()
    )