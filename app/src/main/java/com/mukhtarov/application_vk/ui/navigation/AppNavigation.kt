package com.mukhtarov.application_vk.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mukhtarov.application_vk.model.AppDetails
import com.mukhtarov.application_vk.model.AppDetailsState
import com.mukhtarov.application_vk.model.Category
import com.mukhtarov.application_vk.model.MockAppsData

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "app_list") {

        // 1. ЭКРАН СПИСКА (ИЗ ЧАСТИ 1)
        composable("app_list") {
            AppListScreen(
                onAppClick = { clickedAppId ->
                    // Переходим на экран деталей, передавая ID
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

            val app = MockAppsData.appsList.find { it.id == appId }

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
                    onBackClick = { navController.popBackStack() }, // Кнопка "назад"
                    onShareClick = {},
                    onInstallClick = {},
                    onReadMoreClick = {},
                    onDeveloperClick = {},
                    onSimilarAppDownloadClick = {},
                    // Возврат в список
                    onShowcaseAppClick = {
                        navController.navigate("app_list") {
                            // Очищаем историю переходов
                            popUpTo("app_list") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}