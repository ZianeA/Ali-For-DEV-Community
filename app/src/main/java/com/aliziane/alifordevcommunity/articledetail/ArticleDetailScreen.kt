package com.aliziane.alifordevcommunity.articledetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.*
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme

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
        LazyColumn(Modifier.padding(innerPadding)) {
            when (val article = uiState.articleDetail) {
                is UiResult.Error -> item {
                    PostErrorState()
                }
                is UiResult.Loading -> item {
                    ProgressIndicator()
                }
                is UiResult.Success -> item(key = article.data.id) {
                    ArticleDetail(article = article.data)
                }
            }
            when (val comments = uiState.comments) {
                is UiResult.Error -> item {
                    CommentsErrorState()
                }
                is UiResult.Loading -> item {
                    ProgressIndicator()
                }
                is UiResult.Success -> {
                    itemsIndexed(items = comments.data, key = { _, c -> c.id }) { _, c ->
                        CommentTree(Modifier.padding(horizontal = 16.dp), c)
                    }
                }
            }
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
        })
}

@Composable
private fun BottomBar(modifier: Modifier = Modifier) {
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