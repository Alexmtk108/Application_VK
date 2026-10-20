package com.mukhtarov.application_vk.model

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

/**
 * Захардкоженные данные для практического задания
 */
object MockAppsData {
    val appsList: List<AppItem> = listOf(
        AppItem(
            id = 1,
            name = "ВКонтакте",
            description = "Социальная сеть: мессенджер, звонки, музыка, клипы и сообщества",
            category = AppCategory.SOCIAL,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 2,
            name = "RuStore",
            description = "Официальный российский магазин мобильных приложений и игр для Android",
            category = AppCategory.TOOLS,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 3,
            name = "Почта Mail.ru",
            description = "Удобный и безопасный почтовый клиент с умной сортировкой писем",
            category = AppCategory.TOOLS,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 4,
            name = "VK Музыка",
            description = "Слушайте любимые треки, альбомы, подкасты и плейлисты даже без интернета",
            category = AppCategory.ENTERTAINMENT,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 5,
            name = "СберБанк Онлайн",
            description = "Управление финансами, быстрые переводы по СБП, оплата счетов и вклады",
            category = AppCategory.FINANCE,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 6,
            name = "2ГИС: Карты и Навигатор",
            description = "Подробные офлайн-карты городов, справочник организаций и навигатор",
            category = AppCategory.TRANSPORT,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 7,
            name = "Ozon",
            description = "Маркетплейс с миллионами товаров, быстрой доставкой и выгодными ценами",
            category = AppCategory.SHOPPING,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 8,
            name = "VK Видео",
            description = "Эксклюзивные шоу, фильмы, сериалы, стримы и видеоролики в высоком качестве",
            category = AppCategory.ENTERTAINMENT,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 9,
            name = "Т-Банк",
            description = "Мобильный банк с кэшбэком рублями, инвестициями и круглосуточной поддержкой",
            category = AppCategory.FINANCE,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 10,
            name = "Облако Mail.ru",
            description = "Надёжное облачное хранилище для автоматической загрузки фото и документов",
            category = AppCategory.TOOLS,
            iconRes = R.drawable.ic_launcher_foreground
        ),
        AppItem(
            id = 11,
            name = "Atomic Heart: Companion",
            description = "Интерактивный справочник и мини-игра по вселенной популярного шутера",
            category = AppCategory.GAMES,
            iconRes = R.drawable.ic_launcher_foreground
        )
    )
}