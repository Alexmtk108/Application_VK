package com.mukhtarov.application_vk.domain.usecase

import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.repository.AppRepository
import javax.inject.Inject

class LoadAppRatingsUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(
        apps: List<AppItem>,
        inParallel: Boolean = true
    ): List<AppItem> {
        if (apps.isEmpty()) return emptyList()

        val ids = apps.map { it.id.toString() }
        val ratings = if (inParallel) {
            repository.loadRatingsInParallel(ids)
        } else {
            repository.loadRatingsSequentially(ids)
        }

        return apps.map { app ->
            app.copy(rating = ratings[app.id.toString()])
        }
    }
}