package com.mukhtarov.application_vk.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mukhtarov.application_vk.domain.model.AppDetailsState
import com.mukhtarov.application_vk.presentation.ui.AppDetailsContent
import com.mukhtarov.application_vk.presentation.ui.AppListRoute
import com.mukhtarov.application_vk.presentation.viewmodel.AppDetailsViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "app_list") {

        composable("app_list") {
            AppListRoute(
                onAppClick = { clickedAppId ->
                    // Переход по ID
                    navController.navigate("app_detail/$clickedAppId")
                }
            )
        }

        // ЭКРАН ДЕТАЛЕЙ С ВИТРИНОЙ
        composable(
            route = "app_detail/{appId}",
            arguments = listOf(navArgument("appId") { type = NavType.StringType })
        ) { backStackEntry ->

            // 1. Получаем ViewModel через Hilt
            val viewModel: AppDetailsViewModel = hiltViewModel()

            // 2. Подписываемся на состояния
            val uiState by viewModel.uiState.collectAsState()
            val showcaseApps by viewModel.showcaseApps.collectAsState()

            // 3. Отрисовываем UI в зависимости от состояния
            when (val state = uiState) {
                is AppDetailsState.Loading -> {
                    // Показываем крутилку загрузки по центру экрана
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is AppDetailsState.Error -> {
                    // Показываем текст ошибки
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Ошибка: ${state.message}", color = MaterialTheme.colorScheme.error)
                    }
                }
                is AppDetailsState.Content -> {
                    AppDetailsContent(
                        content = state,
                        showcaseApps = showcaseApps,
                        onBackClick = { navController.popBackStack() },
                        onShareClick = {},
                        onInstallClick = {},
                        onReadMoreClick = {},
                        onDeveloperClick = {},
                        onSimilarAppDownloadClick = {},
                        onShowcaseAppClick = {
                            navController.navigate("app_list") {
                                popUpTo("app_list") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}