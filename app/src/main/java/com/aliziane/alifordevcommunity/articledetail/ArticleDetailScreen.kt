package com.aliziane.alifordevcommunity.articledetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.*
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar

@Composable
fun ArticleDetailScreen(
    articleDetailViewModel: ArticleDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by articleDetailViewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(onBack) },
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        var commentCount by remember { mutableStateOf(0) }
        var foldedComments by remember { mutableStateOf(emptySet<String>()) }

        LazyColumn(Modifier.padding(innerPadding)) {
            when (val article = uiState.articleDetail) {
                is UiResult.Error -> item {
                    PostErrorState()
                }
                is UiResult.Loading -> item {
                    ProgressIndicator()
                }
                is UiResult.Success -> item(key = article.data.id) {
                    commentCount = article.data.commentCount
                    ArticleDetail(article = article.data)
                }
            }

            item {
                Divider()
                Discussion(
                    modifier = Modifier.padding(16.dp),
                    commentCount = commentCount,
                    onSubscribe = { /*TODO*/ }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                CommentInput(
                    Modifier.padding(horizontal = 16.dp),
                    avatarUrl = ""
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            when (val comments = uiState.comments) {
                is UiResult.Error -> item {
                    CommentsErrorState()
                }
                is UiResult.Loading -> item {
                    ProgressIndicator()
                }
                is UiResult.Success -> {
                    for (comment in comments.data) {
                        buildCommentTree(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            comment = comment,
                            isFolded = { id -> foldedComments.contains(id) },
                            onFold = { id -> foldedComments = foldedComments + id },
                            onUnfold = { id -> foldedComments = foldedComments - id }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Discussion(modifier: Modifier = Modifier, commentCount: Int, onSubscribe: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.discussion, commentCount.toPrettyCount()),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        OutlinedButton(onClick = onSubscribe) {
            Text(text = stringResource(R.string.button_subscribe))
        }
    }
}

@Composable
private fun CommentsErrorState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.error_loading_comments))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.button_retry))
        }
    }
}

@Composable
private fun PostErrorState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.error_loading_post))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.button_retry))
        }
    }
}

@Composable
private fun ProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Article Detail")
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars)
    )
}

@Composable
private fun BottomBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(), elevation = 8.dp
    ) {
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

@Preview
@Composable
private fun FooterPreview() {
    AliForDEVCommunityTheme {
        Surface() {
            BottomBar()
        }
    }
}

@Preview
@Composable
private fun PostErrorStatePreview() {
    AliForDEVCommunityTheme {
        Surface {
            PostErrorState()
        }
    }
}

@Preview
@Composable
private fun CommentsErrorStatePreview() {
    AliForDEVCommunityTheme {
        Surface {
            CommentsErrorState()
        }
    }
}

@Preview
@Composable
private fun DiscussionPreview() {
    AliForDEVCommunityTheme {
        Surface {
            Discussion(
                modifier = Modifier.padding(16.dp),
                commentCount = 1000,
                onSubscribe = { /*TODO*/ })
        }
    }
}