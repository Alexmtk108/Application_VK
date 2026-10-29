package com.mukhtarov.application_vk.data.source

import com.mukhtarov.application_vk.R
import com.mukhtarov.application_vk.data.dto.AppDto
import com.mukhtarov.application_vk.domain.model.AppCategory
import com.mukhtarov.application_vk.domain.model.AppItem

object MockAppsDataSource {
    val appsList: List<AppDto> = listOf(
        AppDto(
            id = 1,
            title = "ВКонтакте",
            desc = "Социальная сеть: мессенджер, звонки, музыка, клипы и сообщества",
            categoryString = AppCategory.SOCIAL.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 2,
            title = "RuStore",
            desc = "Официальный российский магазин мобильных приложений и игр для Android",
            categoryString = AppCategory.TOOLS.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 3,
            title = "Почта Mail.ru",
            desc = "Удобный и безопасный почтовый клиент с умной сортировкой писем",
            categoryString = AppCategory.TOOLS.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 4,
            title = "VK Музыка",
            desc = "Слушайте любимые треки, альбомы, подкасты и плейлисты даже без интернета",
            categoryString = AppCategory.ENTERTAINMENT.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 5,
            title = "СберБанк Онлайн",
            desc = "Управление финансами, быстрые переводы по СБП, оплата счетов и вклады",
            categoryString = AppCategory.FINANCE.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 6,
            title = "2ГИС: Карты и Навигатор",
            desc = "Подробные офлайн-карты городов, справочник организаций и навигатор",
            categoryString = AppCategory.TRANSPORT.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 7,
            title = "Ozon",
            desc = "Маркетплейс с миллионами товаров, быстрой доставкой и выгодными ценами",
            categoryString = AppCategory.SHOPPING.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 8,
            title = "VK Видео",
            desc = "Эксклюзивные шоу, фильмы, сериалы, стримы и видеоролики в высоком качестве",
            categoryString = AppCategory.ENTERTAINMENT.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 9,
            title = "Т-Банк",
            desc = "Мобильный банк с кэшбэком рублями, инвестициями и круглосуточной поддержкой",
            categoryString = AppCategory.FINANCE.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 10,
            title = "Облако Mail.ru",
            desc = "Надёжное облачное хранилище для автоматической загрузки фото и документов",
            categoryString = AppCategory.TOOLS.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        ),
        AppDto(
            id = 11,
            title = "Atomic Heart: Companion",
            desc = "Интерактивный справочник и мини-игра по вселенной популярного шутера",
            categoryString = AppCategory.GAMES.toString(),
            iconResId = R.drawable.ic_launcher_foreground
        )
    )
}

