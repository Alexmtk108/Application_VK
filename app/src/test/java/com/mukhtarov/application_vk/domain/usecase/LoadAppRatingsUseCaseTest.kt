package com.mukhtarov.application_vk.domain.usecase

import com.mukhtarov.application_vk.domain.model.AppCategory
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.repository.AppRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoadAppRatingsUseCaseTest {

    private lateinit var repository: AppRepository
    private lateinit var useCase: LoadAppRatingsUseCase

    private val sampleApps = listOf(
        AppItem("1", "ВКонтакте", "Соцсеть", AppCategory.SOCIAL, 1),
        AppItem("2", "RuStore", "Магазин", AppCategory.TOOLS, 2),
        AppItem("3", "Ozon", "Покупки", AppCategory.SHOPPING, 3)
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = LoadAppRatingsUseCase(repository)
    }

    @Test
    fun `invoke with inParallel true calls loadRatingsInParallel and applies ratings`() = runTest {
        val ratingsMap = mapOf("1" to 4.9f, "2" to 4.5f, "3" to 4.2f)
        coEvery { repository.loadRatingsInParallel(listOf("1", "2", "3")) } returns ratingsMap

        val result = useCase(apps = sampleApps, inParallel = true)

        assertEquals(4.9f, result[0].rating)
        assertEquals(4.5f, result[1].rating)
        assertEquals(4.2f, result[2].rating)
        coVerify(exactly = 1) { repository.loadRatingsInParallel(listOf("1", "2", "3")) }
        coVerify(exactly = 0) { repository.loadRatingsSequentially(any()) }
    }

    @Test
    fun `invoke with inParallel false calls loadRatingsSequentially and applies ratings`() = runTest {
        val ratingsMap = mapOf("1" to 3.8f, "2" to 4.1f, "3" to 5.0f)
        coEvery { repository.loadRatingsSequentially(listOf("1", "2", "3")) } returns ratingsMap

        val result = useCase(apps = sampleApps, inParallel = false)

        assertEquals(3.8f, result[0].rating)
        assertEquals(4.1f, result[1].rating)
        assertEquals(5.0f, result[2].rating)
        coVerify(exactly = 1) { repository.loadRatingsSequentially(listOf("1", "2", "3")) }
        coVerify(exactly = 0) { repository.loadRatingsInParallel(any()) }
    }

    @Test
    fun `invoke with empty list returns empty list without calling repository`() = runTest {
        val result = useCase(apps = emptyList(), inParallel = true)

        assertTrue(result.isEmpty())
        coVerify(exactly = 0) { repository.loadRatingsInParallel(any()) }
        coVerify(exactly = 0) { repository.loadRatingsSequentially(any()) }
    }

    @Test
    fun `invoke keeps rating null for apps missing from ratings map`() = runTest {
        val partialRatings = mapOf("1" to 4.7f)
        coEvery { repository.loadRatingsInParallel(any()) } returns partialRatings

        val result = useCase(apps = sampleApps, inParallel = true)

        assertEquals(4.7f, result[0].rating)
        assertNull(result[1].rating)
        assertNull(result[2].rating)
    }

    @Test
    fun `invoke preserves original order and non-rating fields of apps`() = runTest {
        coEvery { repository.loadRatingsInParallel(any()) } returns mapOf("2" to 4.4f)

        val result = useCase(apps = sampleApps, inParallel = true)

        assertEquals(3, result.size)
        assertEquals("1", result[0].id)
        assertEquals("ВКонтакте", result[0].name)
        assertEquals("2", result[1].id)
        assertEquals("RuStore", result[1].name)
        assertEquals(AppCategory.TOOLS, result[1].category)
        assertEquals("3", result[2].id)
        assertEquals("Ozon", result[2].name)
    }

    @Test
    fun `invoke propagates exception thrown by repository`() = runTest {
        coEvery { repository.loadRatingsInParallel(any()) } throws IllegalStateException("Server error")

        val outcome = runCatching {
            useCase(apps = sampleApps, inParallel = true)
        }

        assertTrue(outcome.isFailure)
        assertTrue(outcome.exceptionOrNull() is IllegalStateException)
        assertEquals("Server error", outcome.exceptionOrNull()?.message)
    }
}