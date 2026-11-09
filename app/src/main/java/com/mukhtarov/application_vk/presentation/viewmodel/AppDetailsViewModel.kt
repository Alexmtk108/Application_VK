package com.mukhtarov.application_vk.presentation.viewmodel

// Импортируй свои классы AppDetails, AppDetailsState и Category (из пакета model)
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukhtarov.application_vk.domain.model.AppDetails
import com.mukhtarov.application_vk.domain.model.AppDetailsState
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.domain.model.Category
import com.mukhtarov.application_vk.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle // Позволяет автоматически вытащить аргументы из навигации
) : ViewModel() {

    // Состояние для самой карточки
    private val _uiState = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val uiState: StateFlow<AppDetailsState> = _uiState.asStateFlow()

    // Состояние для витрины внизу экрана
    private val _showcaseApps = MutableStateFlow<List<AppItem>>(emptyList())
    val showcaseApps: StateFlow<List<AppItem>> = _showcaseApps.asStateFlow()

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
                // 1. Идем в сеть за всем каталогом (этот запрос точно работает)
                val allApps = repository.getApps()
                _showcaseApps.value = allApps

                // 2. Ищем наше приложение прямо в скачанном каталоге!
                // Обходим сломанный/неготовый эндпоинт /{id}
                val app = allApps.find { it.id.toString() == appId }

                if (app != null) {
                    // 3. Формируем состояние экрана
                    _uiState.value = AppDetailsState.Content(
                        appDetails = AppDetails(
                            id = app.id.toString(),
                            name = app.name,
                            developer = "VK Play", // Заглушка
                            category = Category.GAME, // Заглушка
                            ageRating = 12,
                            size = 150f,
                            screenshotUrlList = emptyList(),
                            iconUrl = app.iconRes.toString(), // Заглушка, если иконки пока локальные
                            description = app.description
                        ),
                        similarApps = emptyList(),
                        descriptionCollapsed = false
                    )
                } else {
                    _uiState.value = AppDetailsState.Error("Приложение с ID $appId не найдено в каталоге")
                }
            } catch (e: Exception) {
                _uiState.value = AppDetailsState.Error("Ошибка сети: ${e.message}")
            }
        }
    }
}