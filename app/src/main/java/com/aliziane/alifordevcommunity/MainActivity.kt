package com.aliziane.alifordevcommunity

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.text.format.DateUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.aliziane.alifordevcommunity.ArticleDetailViewModel.Companion.KEY_ARTICLE_ID
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import dagger.hilt.android.AndroidEntryPoint
import java.net.URI

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AliForDEVCommunityTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(hiltViewModel()) { articleId ->
                            navController.navigate("articleDetail/$articleId")
                        }
                    }
                    composable(
                        route = "articleDetail/{$KEY_ARTICLE_ID}",
                        arguments = listOf(navArgument(KEY_ARTICLE_ID) { type = NavType.LongType })
                    ) {
                        ArticleDetailScreen(hiltViewModel())
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleDetailScreen(articleDetailViewModel: ArticleDetailViewModel) {
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
                is UiResult.Success<ArticleDetail> -> ArticleDetail(article = result.data)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ArticleDetail(article: ArticleDetail) {
    Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
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

        Column(modifier = Modifier.padding(8.dp)) {

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

            SelectionContainer {
                Column {
                    MaterialRichText(modifier = Modifier.padding(8.dp)) {
                        Markdown(checkNotNull(article.bodyMarkdown))
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(homeViewModel: HomeViewModel, onClick: (articleId: Long) -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        val articlesResult by homeViewModel.articles.collectAsState()

        when (val result = articlesResult) {
            is UiResult.Error -> TODO()
            is UiResult.Loading -> TODO()
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
                shape = RoundedCornerShape(size = 8.dp)
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
                        Spacer(modifier = Modifier.width(8.dp))
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

@Composable
private fun Tag(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    val hashColor = MaterialTheme.colors.onSurface.copy(alpha = 0.28f)
    val textColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)

    ClickableText(
        modifier = modifier,
        text = buildAnnotatedString {
            pushStyle(SpanStyle(hashColor))
            append("#")

            pop()
            pushStyle(SpanStyle(textColor))
            append(text)
        },
        style = MaterialTheme.typography.body1,
        onClick = { onClick() }
    )
}

@Composable
private fun HyperlinkTag(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    ClickableText(
        modifier = modifier,
        text = AnnotatedString(text, spanStyle = SpanStyle(Color(0xFF007BFF))),
        onClick = { onClick() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Chip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        onClick = onClick,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
        )
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.body2) {
            Row(modifier = Modifier.padding(paddingValues = contentPadding)) {
                content()
            }
        }
    }
}

private val fakeArticleUser = Article.User(
    name = "Ali Ziane",
    avatarUrl = "https://avatars.githubusercontent.com/u/14791787?s=400&v=4"
)

private val fakeArticle = Article(
    id = 101,
    title = "Jetpack Compose 1.0 is out!",
    description = "The new Android UI toolkit is finally here. See how it compares to the established view-based UI approach...",
    reactionCount = 4058,
    commentCount = 25,
    coverImageUrl = "https://cdna.artstation.com/p/assets/images/images/021/777/836/large/ali-ziane-final.jpg?1572910336",
    readTimeInMinutes = 3,
    url = "https://dev.to/",
    canonicalUrl = "https://github.com/ZianeA",
    publishedAt = Iso8601Utils.parse("2021-09-08T00:00:00Z"),
    editedAt = null,
    tags = listOf("android", "kotlin", "compose"),
    author = fakeArticleUser
)

private val fakeArticleDetailUser = fakeArticleUser.run { ArticleDetail.User(name, avatarUrl) }

private val fakeArticleDetail = fakeArticle.run {
    ArticleDetail(
        id,
        title,
        description,
        FAKE_BODY_MARKDOWN,
        commentCount,
        reactionCount,
        coverImageUrl,
        readTimeInMinutes,
        url,
        canonicalUrl,
        publishedAt,
        editedAt,
        tags,
        fakeArticleDetailUser,
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
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

@Preview
@Composable
private fun ArticleDetailPreview() {
    AliForDEVCommunityTheme {
        Surface(color = MaterialTheme.colors.background) {
            ArticleDetail(article = fakeArticleDetail)
        }
    }
}

