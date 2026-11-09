package com.mukhtarov.application_vk.data.source

import com.mukhtarov.application_vk.data.dto.AppDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AppApiService {

    // 1. Запрос списка приложений
    @GET("catalog")
    suspend fun getCatalog(): List<AppDto>

    // 2. Запрос конкретного приложения
    @GET("{id}")
    suspend fun getAppById(@Path("id") id: String): AppDto
}