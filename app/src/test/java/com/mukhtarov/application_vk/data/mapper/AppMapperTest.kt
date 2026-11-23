package com.mukhtarov.application_vk.data.mapper

import com.mukhtarov.application_vk.R
import com.mukhtarov.application_vk.data.dto.AppDto
import com.mukhtarov.application_vk.domain.model.AppCategory
import com.mukhtarov.application_vk.domain.model.AppItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppMapperTest {

    @Test
    fun `toDomain maps valid AppDto to AppItem correctly`() {
        val dto = AppDto(
            id = "app-1",
            title = "ВКонтакте",
            desc = "Социальная сеть",
            categoryString = "SOCIAL",
            iconUrl = "https://example.com/vk.png"
        )

        val domain = dto.toDomain()

        assertEquals("app-1", domain.id)
        assertEquals("ВКонтакте", domain.name)
        assertEquals("Социальная сеть", domain.description)
        assertEquals(AppCategory.SOCIAL, domain.category)
        assertEquals(R.drawable.ic_launcher_foreground, domain.iconRes)
        assertNull(domain.rating)
    }

    @Test
    fun `toDomain maps lowercase and spaced category string correctly`() {
        val dto = AppDto(
            id = "app-2",
            title = "СберБанк",
            desc = "Банк",
            categoryString = "  finance  ",
            iconUrl = "https://example.com/sber.png"
        )

        val domain = dto.toDomain()

        assertEquals(AppCategory.FINANCE, domain.category)
    }

    @Test
    fun `toDomain falls back to TOOLS when category is unknown`() {
        val dto = AppDto(
            id = "app-3",
            title = "Тест",
            desc = "Описание",
            categoryString = "UNKNOWN_CATEGORY",
            iconUrl = ""
        )

        val domain = dto.toDomain()

        assertEquals(AppCategory.TOOLS, domain.category)
    }

    @Test
    fun `toDomain handles null fields safely with defaults`() {
        val dto = AppDto(
            id = "app-4",
            title = null,
            desc = null,
            categoryString = null,
            iconUrl = null
        )

        val domain = dto.toDomain()

        assertEquals("app-4", domain.id)
        assertEquals("", domain.name)
        assertEquals("", domain.description)
        assertEquals(AppCategory.TOOLS, domain.category)
        assertEquals(R.drawable.ic_launcher_foreground, domain.iconRes)
    }

    @Test
    fun `toDto maps AppItem to AppDto correctly`() {
        val domain = AppItem(
            id = "app-5",
            name = "RuStore",
            description = "Магазин приложений",
            category = AppCategory.TOOLS,
            iconRes = R.drawable.ic_launcher_foreground,
            rating = 4.8f
        )

        val dto = domain.toDto()

        assertEquals("app-5", dto.id)
        assertEquals("RuStore", dto.title)
        assertEquals("Магазин приложений", dto.desc)
        assertEquals("TOOLS", dto.categoryString)
    }

    @Test
    fun `round-trip mapping from Domain to DTO and back preserves data`() {
        val original = AppItem(
            id = "app-6",
            name = "Ozon",
            description = "Маркетплейс",
            category = AppCategory.SHOPPING,
            iconRes = R.drawable.ic_launcher_foreground,
            rating = null
        )

        val restored = original.toDto().toDomain()

        assertEquals(original, restored)
    }
}