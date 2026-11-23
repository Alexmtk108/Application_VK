package com.mukhtarov.application_vk.data.source

import com.mukhtarov.application_vk.data.dto.AppDto
import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.coroutines.delay

interface AppApiService {

    // 1. Запрос списка приложений
    @GET("catalog")
    suspend fun getCatalog(): List<AppDto>

    // 2. Запрос конкретного приложения
    @GET("{id}")
    suspend fun getAppById(@Path("id") id: String): AppDto
}

    // Эмуляция сетевого запроса получения рейтинга.
    // Вынесено в extension-функцию, чтобы Retrofit Proxy не требовал @GET-аннотацию.
    suspend fun AppApiService.getRating(id: String): Float {
        delay(1500)
        return (30..50).random() / 10f
    }
