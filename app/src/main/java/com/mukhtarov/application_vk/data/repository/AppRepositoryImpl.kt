package com.mukhtarov.application_vk.data.repository

import com.mukhtarov.application_vk.data.mapper.toDomain
import com.mukhtarov.application_vk.data.source.MockAppsDataSource
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.repository.AppRepository

class AppRepositoryImpl : AppRepository {

    override fun getApps(): List<AppItem> {
        // 1. Получаем список DTO из источника данных (Mock)
        val dtoList = MockAppsDataSource.appsList

        // 2. Конвертируем каждый AppDto в AppItem через маппер (.toDomain())
        return dtoList.map { it.toDomain() }
    }

    override fun getAppById(id: Int): AppItem? {
        val dto = MockAppsDataSource.appsList.find { it.id == id }
        return dto?.toDomain()
    }
}