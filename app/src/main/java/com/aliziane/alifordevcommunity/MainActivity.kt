package com.aliziane.alifordevcommunity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
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

