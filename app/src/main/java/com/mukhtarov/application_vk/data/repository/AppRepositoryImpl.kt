package com.mukhtarov.application_vk.data.repository

import com.mukhtarov.application_vk.data.mapper.toDomain
import com.mukhtarov.application_vk.data.source.AppApiService
import com.mukhtarov.application_vk.data.source.MockAppsDataSource
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.repository.AppRepository
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
}