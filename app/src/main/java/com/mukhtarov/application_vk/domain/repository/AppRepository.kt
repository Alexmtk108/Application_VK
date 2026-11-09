package com.mukhtarov.application_vk.domain.repository

import com.mukhtarov.application_vk.domain.model.AppItem

interface AppRepository {
    fun getApps(): List<AppItem>
    fun getAppById(id: Int): AppItem?
}