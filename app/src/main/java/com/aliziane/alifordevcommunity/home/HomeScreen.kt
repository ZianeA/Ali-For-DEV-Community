package com.aliziane.alifordevcommunity.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliziane.alifordevcommunity.common.*
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToArticle: (articleId: Long) -> Unit,
    onOpenDrawer: () -> Unit,
    scaffoldState: ScaffoldState
) {
    Scaffold(scaffoldState = scaffoldState, topBar = { TopBar(onOpenDrawer) }) {
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
                        Article(
                            article = article,
                            onClick = { onNavigateToArticle(article.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(onOpenDrawer: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Home")
        },
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer")
            }
        })
}