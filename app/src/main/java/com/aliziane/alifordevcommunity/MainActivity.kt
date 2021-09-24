package com.aliziane.alifordevcommunity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.aliziane.alifordevcommunity.articledetail.ArticleDetailScreen
import com.aliziane.alifordevcommunity.articledetail.ArticleDetailViewModel.Companion.KEY_ARTICLE_ID
import com.aliziane.alifordevcommunity.home.HomeScreen
import com.aliziane.alifordevcommunity.ui.theme.AliForDEVCommunityTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            AliForDEVCommunityTheme {
                Scaffold(scaffoldState = scaffoldState, drawerContent = { Drawer() }) {
                    NavGraph(navController, coroutineScope, scaffoldState)
                }
            }
        }
    }
}

@Composable
private fun NavGraph(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                homeViewModel = hiltViewModel(),
                onNavigateToArticle = { articleId ->
                    navController.navigate("articleDetail/$articleId")
                },
                onOpenDrawer = { coroutineScope.launch { scaffoldState.drawerState.open() } },
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = "articleDetail/{$KEY_ARTICLE_ID}",
            arguments = listOf(navArgument(KEY_ARTICLE_ID) {
                type = NavType.LongType
            })
        ) {
            ArticleDetailScreen(
                articleDetailViewModel = hiltViewModel(),
                onBack = { navController.navigateUp() }
            )
        }
    }
}

@Composable
private fun Drawer() {
    Column {
        Text(
            text = "DEV Community",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Divider()
        var isSelected by remember { mutableStateOf(false) }
        DrawerButton(
            icon = Icons.Outlined.Home,
            label = "Home",
            isSelected = isSelected,
            onClick = { isSelected = !isSelected }
        )
    }
}

@Composable
private fun DrawerButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val textIconColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.6f)
    val backgroundColor = if (isSelected) colors.primary.copy(alpha = 0.12f) else Color.Transparent

    Surface(
        modifier = modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = null, // decorative
                    colorFilter = ColorFilter.tint(textIconColor),
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun DrawerPreview() {
    AliForDEVCommunityTheme {
        Surface {
            Drawer()
        }
    }
}

