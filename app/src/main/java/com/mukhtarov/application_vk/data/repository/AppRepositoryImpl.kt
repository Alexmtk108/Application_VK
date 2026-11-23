package com.mukhtarov.application_vk.data.repository

import com.mukhtarov.application_vk.data.mapper.toDomain
import com.mukhtarov.application_vk.data.source.AppApiService
import com.mukhtarov.application_vk.data.source.getRating
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.repository.AppRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiService: AppApiService
) : AppRepository {

    override suspend fun getApps(): List<AppItem> {
        return try {
            val dtoList = apiService.getCatalog()
            dtoList.map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAppById(id: String): AppItem? {
        return try {
            val dto = apiService.getAppById(id)
            dto.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 1. Последовательная загрузка: ждём каждый запрос по очереди
    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
        val result = mutableMapOf<String, Float>()
        for (id in ids) {
            result[id] = apiService.getRating(id)
        }
        return result
    }

    // 2. Параллельная загрузка без ограничений: запускаем все запросы одновременно
    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> =
        coroutineScope {
            ids.map { id ->
                async { id to apiService.getRating(id) }
            }.awaitAll().toMap()
        }
}