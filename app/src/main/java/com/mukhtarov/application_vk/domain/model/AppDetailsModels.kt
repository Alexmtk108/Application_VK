package com.mukhtarov.application_vk.domain.model

// 1. Категория для карточки
enum class Category { GAME, APP, OTHER }

// 2. Модель похожего приложения для карусели в карточке
data class SimilarApp(
    val id: String,
    val name: String,
    val iconUrl: String
)

// 3. Основная модель данных детального экрана
data class AppDetails(
    val id: String,
    val name: String,
    val developer: String,
    val category: Category,
    val ageRating: Int,
    val size: Float,
    val screenshotUrlList: List<String>,
    val iconUrl: String,
    val description: String
)

// 4. Состояние экрана
object AppDetailsState {
    data class Content(
        val appDetails: AppDetails,
        val similarApps: List<SimilarApp>,
        val descriptionCollapsed: Boolean
    )
}