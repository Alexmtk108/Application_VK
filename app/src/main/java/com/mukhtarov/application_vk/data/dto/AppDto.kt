package com.mukhtarov.application_vk.data.dto

import com.google.gson.annotations.SerializedName

data class AppDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val title: String?, // Пример: в JSON "name", а у нас val title
    @SerializedName("description") val desc: String?,
    @SerializedName("category") val categoryString: String?,
    @SerializedName("iconUrl") val iconUrl: String? // С сервера обычно приходит URL, а не Int-ресурс
)