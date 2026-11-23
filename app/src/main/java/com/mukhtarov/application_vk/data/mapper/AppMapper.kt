package com.mukhtarov.application_vk.data.mapper

import com.mukhtarov.application_vk.R
import com.mukhtarov.application_vk.data.dto.AppDto
import com.mukhtarov.application_vk.domain.model.AppCategory
import com.mukhtarov.application_vk.domain.model.AppItem

fun AppDto.toDomain(): AppItem {
    return AppItem(
        id = this.id,
        name = this.title.orEmpty(),
        description = this.desc.orEmpty(),
        category = mapCategory(this.categoryString),
        iconRes = R.drawable.ic_launcher_foreground
    )
}

fun AppItem.toDto(): AppDto {
    return AppDto(
        id = this.id,
        title = this.name,
        desc = this.description,
        categoryString = this.category.name,
        iconUrl = ""
    )
}

private fun mapCategory(categoryString: String?): AppCategory {
    if (categoryString.isNullOrBlank()) return AppCategory.TOOLS
    return try {
        AppCategory.valueOf(categoryString.trim().uppercase())
    } catch (e: IllegalArgumentException) {
        AppCategory.TOOLS
    }
}