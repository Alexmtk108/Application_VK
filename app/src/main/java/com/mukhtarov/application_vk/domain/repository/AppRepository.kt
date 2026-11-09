package com.mukhtarov.application_vk.domain.repository

import com.mukhtarov.application_vk.domain.model.AppItem

interface AppRepository {
    suspend fun getApps(): List<AppItem>
    suspend fun getAppById(id: String): AppItem?
}