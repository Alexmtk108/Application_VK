package com.mukhtarov.application_vk.domain.repository

import com.mukhtarov.application_vk.domain.model.AppItem

interface AppRepository {
    suspend fun getApps(): List<AppItem>
    suspend fun getAppById(id: String): AppItem?

    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float>
    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float>
}