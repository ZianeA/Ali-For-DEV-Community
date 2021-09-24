package com.aliziane.alifordevcommunity.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aliziane.alifordevcommunity.common.*

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToArticle: (articleId: Long) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState, topBar = { TopBar(onOpenDrawer) }) {
        val articlesResult by homeViewModel.articles.collectAsState()

        when (val result = articlesResult) {
            is UiResult.Error -> ErrorState(scaffoldState, result.error)
            is UiResult.Loading -> LoadingState()
            is UiResult.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(result.data) { article ->
                        Article(article = article, onClick = { onNavigateToArticle(article.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(scaffoldState: ScaffoldState, @StringRes error: Int) {
    val message = stringResource(id = error)
    LaunchedEffect(scaffoldState.snackbarHostState) {
        scaffoldState.snackbarHostState.showSnackbar(message)
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