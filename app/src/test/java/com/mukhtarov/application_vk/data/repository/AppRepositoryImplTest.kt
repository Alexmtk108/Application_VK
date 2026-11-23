package com.mukhtarov.application_vk.data.repository

import com.mukhtarov.application_vk.data.dto.AppDto
import com.mukhtarov.application_vk.data.source.AppApiService
import com.mukhtarov.application_vk.domain.model.AppCategory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AppRepositoryImplTest {

    private lateinit var apiService: AppApiService
    private lateinit var repository: AppRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        repository = AppRepositoryImpl(apiService)
    }

    @Test
    fun `getApps returns mapped domain list when api succeeds`() = runTest {
        val dtoList = listOf(
            AppDto("1", "VK", "Соцсеть", "SOCIAL", "url1"),
            AppDto("2", "2GIS", "Карты", "TRANSPORT", "url2")
        )
        coEvery { apiService.getCatalog() } returns dtoList

        val result = repository.getApps()

        assertEquals(2, result.size)
        assertEquals("VK", result[0].name)
        assertEquals(AppCategory.SOCIAL, result[0].category)
        assertEquals("2GIS", result[1].name)
        assertEquals(AppCategory.TRANSPORT, result[1].category)
        coVerify(exactly = 1) { apiService.getCatalog() }
    }

    @Test
    fun `getApps returns empty list when api returns empty list`() = runTest {
        coEvery { apiService.getCatalog() } returns emptyList()

        val result = repository.getApps()

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { apiService.getCatalog() }
    }

    @Test
    fun `getApps returns empty list when api throws exception`() = runTest {
        coEvery { apiService.getCatalog() } throws IOException("No internet")

        val result = repository.getApps()

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { apiService.getCatalog() }
    }

    @Test
    fun `getAppById returns mapped AppItem when api succeeds`() = runTest {
        val dto = AppDto("42", "RuStore", "Каталог", "TOOLS", "url42")
        coEvery { apiService.getAppById("42") } returns dto

        val result = repository.getAppById("42")

        assertNotNull(result)
        assertEquals("42", result?.id)
        assertEquals("RuStore", result?.name)
        assertEquals(AppCategory.TOOLS, result?.category)
        coVerify(exactly = 1) { apiService.getAppById("42") }
    }

    @Test
    fun `getAppById returns null when api throws exception`() = runTest {
        coEvery { apiService.getAppById("999") } throws RuntimeException("404 Not Found")

        val result = repository.getAppById("999")

        assertNull(result)
        coVerify(exactly = 1) { apiService.getAppById("999") }
    }

    @Test
    fun `loadRatingsSequentially loads all ratings one by one`() = runTest {
        val ids = listOf("1", "2", "3")
        val startTime = testScheduler.currentTime

        val ratings = repository.loadRatingsSequentially(ids)
        val elapsedVirtualTime = testScheduler.currentTime - startTime

        assertEquals(3, ratings.size)
        ids.forEach { id ->
            val rating = ratings[id]
            assertNotNull(rating)
            assertTrue(rating!! in 3.0f..5.0f)
        }
        // 3 запроса по 1500 мс последовательно = 4500 мс виртуального времени
        assertEquals(4500L, elapsedVirtualTime)
    }

    @Test
    fun `loadRatingsInParallel loads all ratings concurrently`() = runTest {
        val ids = listOf("1", "2", "3")
        val startTime = testScheduler.currentTime

        val ratings = repository.loadRatingsInParallel(ids)
        val elapsedVirtualTime = testScheduler.currentTime - startTime

        assertEquals(3, ratings.size)
        ids.forEach { id ->
            val rating = ratings[id]
            assertNotNull(rating)
            assertTrue(rating!! in 3.0f..5.0f)
        }
        // Все 3 запроса стартуют одновременно = 1500 мс виртуального времени
        assertEquals(1500L, elapsedVirtualTime)
    }
}