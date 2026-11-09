package com.mukhtarov.application_vk.data.mapper

import com.mukhtarov.application_vk.R
import com.mukhtarov.application_vk.data.dto.AppDto
import com.mukhtarov.application_vk.domain.model.AppCategory
import com.mukhtarov.application_vk.domain.model.AppItem

// Extension-функция для конвертации
fun AppDto.toDomain(): AppItem {
    return AppItem(
        id = this.id,
        name = this.title ?: "Без названия",
        description = this.desc ?: "Нет описания",
        category = mapCategory(this.categoryString ?: ""),
        iconRes = R.drawable.ic_launcher_foreground
    )
}

private fun mapCategory(categoryString: String): AppCategory {
    return try {
        AppCategory.valueOf(categoryString)
    } catch (e: Exception) {
        AppCategory.TOOLS // Значение по умолчанию, если пришла неизвестная категория
    }
}
