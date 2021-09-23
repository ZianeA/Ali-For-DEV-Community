package com.aliziane.alifordevcommunity.articledetail

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliziane.alifordevcommunity.R
import com.aliziane.alifordevcommunity.common.*
import com.aliziane.alifordevcommunity.common.network.Iso8601Utils
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme

@Composable
fun ArticleDetailScreen(
    articleDetailViewModel: ArticleDetailViewModel,
    onBack: () -> Unit,
    scaffoldState: ScaffoldState
) {
    val uiState by articleDetailViewModel.uiState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(onBack) },
        bottomBar = { BottomBar() }
    ) {
        LazyColumn {
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
        Surface(color = MaterialTheme.colors.background) {
            BottomBar()
        }
    }
}