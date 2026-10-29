package com.mukhtarov.application_vk.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mukhtarov.application_vk.data.repository.AppRepositoryImpl
import com.mukhtarov.application_vk.data.source.MockAppsDataSource
import com.mukhtarov.application_vk.domain.model.AppDetails
import com.mukhtarov.application_vk.domain.model.AppDetailsState
import com.mukhtarov.application_vk.domain.model.Category
import com.mukhtarov.application_vk.presentation.ui.AppDetailsContent
import com.mukhtarov.application_vk.presentation.ui.AppListRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "app_list") {

        composable("app_list") {
            AppListRoute(
                onAppClick = { clickedAppId ->
                    navController.navigate("app_detail/$clickedAppId")
                }
            )
        }

        // 2. ЭКРАН ДЕТАЛЕЙ С ВИТРИНОЙ
        composable(
            route = "app_detail/{appId}",
            arguments = listOf(navArgument("appId") { type = NavType.IntType })
        ) { backStackEntry ->
            val appId = backStackEntry.arguments?.getInt("appId") ?: return@composable
            val repository = AppRepositoryImpl()

            val app = repository.getAppById(appId)
            val allApps = repository.getApps()

            if (app != null) {
                val mockContent = AppDetailsState.Content(
                    appDetails = AppDetails(
                        id = app.id.toString(),
                        name = app.name,
                        developer = "VK Play", // Заглушка
                        category = Category.GAME, // Заглушка
                        ageRating = 12,
                        size = 150f,
                        screenshotUrlList = emptyList(),
                        iconUrl = "https://example.com/icon", // Заглушка
                        description = app.description
                    ),
                    similarApps = emptyList(),
                    descriptionCollapsed = false
                )

                AppDetailsContent(
                    content = mockContent,
                    showcaseApps = allApps, // ИСПРАВЛЕНО: передаем данные для витрины из репозитория
                    onBackClick = { navController.popBackStack() }, // Кнопка "назад"
                    onShareClick = {},
                    onInstallClick = {},
                    onReadMoreClick = {},
                    onDeveloperClick = {},
                    onSimilarAppDownloadClick = {},
                    // Возврат в список
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