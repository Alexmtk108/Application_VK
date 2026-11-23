package com.mukhtarov.application_vk.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

// Состояние экрана (хранит список)
data class AppListUiState(
    val apps: List<AppItem> = emptyList(),
    val isRatingsLoading: Boolean = false,
    val sequentialTimeMs: Long? = null,
    val parallelTimeMs: Long? = null
)

// Одноразовые события экрана
class AppListUiEvent {
    val apps: List<AppItem> = emptyList()
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var ratingsJob: Job? = null

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            val apps = repository.getApps()
            _uiState.update { it.copy(apps = apps) }
            // Автоматически подгружаем рейтинги параллельно при первом открытии
            loadRatingsInParallel()
        }
    }

    fun loadRatingsSequentially() {
        val currentApps = _uiState.value.apps
        if (currentApps.isEmpty()) return

        ratingsJob?.cancel()
        ratingsJob = viewModelScope.launch {
            // Сбрасываем старые рейтинги и включаем индикатор загрузки
            _uiState.update { state ->
                state.copy(
                    isRatingsLoading = true,
                    apps = state.apps.map { it.copy(rating = null) }
                )
            }

            val ids = currentApps.map { it.id.toString() }
            var ratings: Map<String, Float> = emptyMap()

            val elapsedMs = measureTimeMillis {
                ratings = repository.loadRatingsSequentially(ids)
            }

            Log.d("RatingsBenchmark", "Последовательно (${ids.size} приложений): $elapsedMs мс")

            _uiState.update { state ->
                state.copy(
                    isRatingsLoading = false,
                    sequentialTimeMs = elapsedMs,
                    apps = state.apps.map { app ->
                        app.copy(rating = ratings[app.id.toString()])
                    }
                )
            }
        }
    }

    fun loadRatingsInParallel() {
        val currentApps = _uiState.value.apps
        if (currentApps.isEmpty()) return

        ratingsJob?.cancel()
        ratingsJob = viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isRatingsLoading = true,
                    apps = state.apps.map { it.copy(rating = null) }
                )
            }

            val ids = currentApps.map { it.id.toString() }
            var ratings: Map<String, Float> = emptyMap()

            val elapsedMs = measureTimeMillis {
                ratings = repository.loadRatingsInParallel(ids)
            }

            Log.d("RatingsBenchmark", "Параллельно (${ids.size} приложений): $elapsedMs мс")

            _uiState.update { state ->
                state.copy(
                    isRatingsLoading = false,
                    parallelTimeMs = elapsedMs,
                    apps = state.apps.map { app ->
                        app.copy(rating = ratings[app.id.toString()])
                    }
                )
            }
        }
    }

    fun onToolbarClicked() {
        // Просто отправляем текст в канал
        _uiEvent.trySend("Привет от Александра! Тулбар нажат \uD83D\uDE80")
    }
}