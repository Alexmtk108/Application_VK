package com.mukhtarov.application_vk.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mukhtarov.application_vk.model.AppItem
import com.mukhtarov.application_vk.model.MockAppsData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

// Состояние экрана (хранит список)
data class AppListUiState(
    val apps: List<AppItem> = emptyList()
)

// Одноразовые события экрана
class AppListUiEvent {
    val apps: List<AppItem> = emptyList()
}

class AppListViewModel : ViewModel() {
    // StateFlow хранит состояние и переживает поворот экрана
    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        // Загрузка данных происходит только один раз при создании ViewModel.
        // При повороте экрана ViewModel не пересоздается, поэтому загрузка не повторится.
        loadApps()
    }

    private fun loadApps() {
        _uiState.value = AppListUiState(apps = MockAppsData.appsList)
    }

    fun onToolbarClicked() {
        // Просто отправляем текст в канал
        _uiEvent.trySend("Привет от Александра! Тулбар нажат \uD83D\uDE80")
    }
}