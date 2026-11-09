package com.mukhtarov.application_vk.domain.model

import androidx.annotation.DrawableRes
import com.mukhtarov.application_vk.R

/**
 * Категории приложений
 */
enum class AppCategory(val title: String) {
    FINANCE("Финансы"),
    TOOLS("Инструменты"),
    SOCIAL("Общение"),
    GAMES("Игры"),
    TRANSPORT("Транспорт"),
    SHOPPING("Покупки"),
    ENTERTAINMENT("Развлечения")
}

/**
 * Модель данных элемента списка приложений.
 */
data class AppItem(
    val id: Int,
    val name: String,
    val description: String,
    val category: AppCategory,
    @DrawableRes val iconRes: Int
)