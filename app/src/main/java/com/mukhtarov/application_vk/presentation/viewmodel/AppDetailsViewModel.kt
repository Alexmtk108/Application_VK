package com.mukhtarov.application_vk.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukhtarov.application_vk.domain.model.AppDetails
import com.mukhtarov.application_vk.domain.model.AppDetailsState
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.model.Category
import com.mukhtarov.application_vk.domain.model.InstallState
import com.mukhtarov.application_vk.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val uiState: StateFlow<AppDetailsState> = _uiState.asStateFlow()

    private val _showcaseApps = MutableStateFlow<List<AppItem>>(emptyList())
    val showcaseApps: StateFlow<List<AppItem>> = _showcaseApps.asStateFlow()

    // Ссылка на активную корутину установки
    private var installJob: Job? = null

    init {
        val appId = savedStateHandle.get<String>("appId")
        if (appId != null) {
            loadAppDetails(appId)
        } else {
            _uiState.value = AppDetailsState.Error("Не передан id приложения")
        }
    }

    private fun loadAppDetails(appId: String) {
        viewModelScope.launch {
            try {
                val allApps = repository.getApps()
                _showcaseApps.value = allApps

                val app = allApps.find { it.id.toString() == appId }

                if (app != null) {
                    _uiState.value = AppDetailsState.Content(
                        appDetails = AppDetails(
                            id = app.id.toString(),
                            name = app.name,
                            developer = "VK Play",
                            category = Category.GAME,
                            ageRating = 12,
                            size = 150f,
                            screenshotUrlList = emptyList(),
                            iconUrl = app.iconRes.toString(),
                            description = app.description
                        ),
                        similarApps = emptyList(),
                        descriptionCollapsed = false
                    )
                } else {
                    _uiState.value = AppDetailsState.Error("Приложение с ID $appId не найдено")
                }
            } catch (e: Exception) {
                _uiState.value = AppDetailsState.Error("Ошибка сети: ${e.message}")
            }
        }
    }

    fun onInstallClick() {
        val current = _uiState.value
        if (current !is AppDetailsState.Content) return

        when (current.installState) {
            is InstallState.Installing -> {
                // Отменяем корутину и сбрасываем состояние в NotInstalled
                installJob?.cancel()
                installJob = null
                _uiState.update { state ->
                    if (state is AppDetailsState.Content) {
                        state.copy(installState = InstallState.NotInstalled)
                    } else state
                }
            }

            is InstallState.NotInstalled -> {
                // Запускаем установку в viewModelScope (при уходе с экрана отменится автоматически)
                installJob = viewModelScope.launch {
                    emulateInstall()
                }
            }

            is InstallState.Installed -> {
                // Уже установлено — ничего не делаем (или можно реализовать удаление)
            }
        }
    }

    private suspend fun emulateInstall() {
        for (progress in 0..100 step 5) {
            _uiState.update { state ->
                if (state is AppDetailsState.Content) {
                    state.copy(installState = InstallState.Installing(progress))
                } else state
            }
            delay(150)
        }
        _uiState.update { state ->
            if (state is AppDetailsState.Content) {
                state.copy(installState = InstallState.Installed)
            } else state
        }
        installJob = null
    }
}