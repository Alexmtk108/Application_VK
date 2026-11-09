package com.mukhtarov.application_vk.data.mapper

import com.mukhtarov.application_vk.data.dto.AppDto
import com.mukhtarov.application_vk.domain.model.AppCategory
import com.mukhtarov.application_vk.domain.model.AppItem

// Extension-функция для конвертации
fun AppDto.toDomain(): AppItem {
    return AppItem(
        id = this.id,
        name = this.title,           // маппинг title -> name
        description = this.desc,     // маппинг desc -> description
        category = mapCategory(this.categoryString), // маппинг строки в Enum
        iconRes = this.iconResId
    )
}

private fun mapCategory(categoryString: String): AppCategory {
    return try {
        AppCategory.valueOf(categoryString)
    } catch (e: Exception) {
        AppCategory.TOOLS // Значение по умолчанию, если пришла неизвестная категория
    }
}
