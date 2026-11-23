package com.mukhtarov.application_vk.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mukhtarov.application_vk.R
import com.mukhtarov.application_vk.domain.model.AppItem
import com.mukhtarov.application_vk.presentation.viewmodel.AppListViewModel
import com.mukhtarov.application_vk.domain.model.AppCategory
import com.mukhtarov.application_vk.presentation.viewmodel.AppListUiState

/**
 * Основной экран со списком приложений.
 * Компоновка сверху вниз: Тулбар -> Список приложений.
 */

/**
 * Stateful-обертка, которая связывает ViewModel с UI.
 */
@Composable
fun AppListRoute(
    onAppClick: (String) -> Unit,
    viewModel: AppListViewModel = hiltViewModel()
) {
    // Подписываемся на состояние списка
    val uiState by viewModel.uiState.collectAsState()

    // Состояние для управления Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Слушаем одноразовые события (Snackbar).
    // LaunchedEffect не перезапускается при повороте экрана, канал отдает ивент 1 раз.
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message = message)
        }
    }

    AppListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAppClick = onAppClick,
        onToolbarClick = { viewModel.onToolbarClicked() },
        onLoadSequentialClick = { viewModel.loadRatingsSequentially() },
        onLoadParallelClick = { viewModel.loadRatingsInParallel() }
    )
}

/**
 * Stateless-экран (только верстка, никаких данных и логики).
 */
@Composable
fun AppListScreen(
    uiState: AppListUiState,
    snackbarHostState: SnackbarHostState,
    onAppClick: (String) -> Unit, // или Int — оставь свой текущий тип
    onToolbarClick: () -> Unit,
    onLoadSequentialClick: () -> Unit,
    onLoadParallelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppListToolbar(onToolbarClick = onToolbarClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Панель тестирования скорости загрузки рейтингов
            RatingsBenchmarkPanel(
                isLoading = uiState.isRatingsLoading,
                sequentialTimeMs = uiState.sequentialTimeMs,
                parallelTimeMs = uiState.parallelTimeMs,
                onSequentialClick = onLoadSequentialClick,
                onParallelClick = onLoadParallelClick
            )

            AppList(
                apps = uiState.apps,
                onAppClick = onAppClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RatingsBenchmarkPanel(
    isLoading: Boolean,
    sequentialTimeMs: Long?,
    parallelTimeMs: Long?,
    onSequentialClick: () -> Unit,
    onParallelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onSequentialClick,
                enabled = !isLoading,
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Последовательно",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Button(
                onClick = onParallelClick,
                enabled = !isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Параллельно", maxLines = 1)
            }
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (sequentialTimeMs != null || parallelTimeMs != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildString {
                    sequentialTimeMs?.let { append("Последовательно: ${it} мс   ") }
                    parallelTimeMs?.let { append("Параллельно: ${it} мс") }
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Тулбар приложения.
 * Содержит иконку текущего приложения слева. Цвет задника — акцентный.
 */
@Composable
fun AppListToolbar(
    onToolbarClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF0077FF)
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToolbarClick() },
        color = accentColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка текущего приложения слева
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Иконка текущего приложения",
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Заголовок в тулбаре
            Text(
                text = "Каталог приложений",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Вертикальный список приложений.
 */
@Composable
fun AppList(
    apps: List<AppItem>,
    onAppClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(
            items = apps,
            key = { app -> app.id }
        ) { app ->
            AppListItem(
                app = app,
                onAppClick = onAppClick, // 2. Передаем клик дальше, в саму карточку
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 88.dp, end = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

/**
 * Элемент списка приложений.
 * Содержит: иконку приложения, название, краткое описание и категорию.
 */
@Composable
fun AppListItem(
    app: AppItem,
    onAppClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onAppClick(app.id) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Иконка приложения
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = app.iconRes),
                contentDescription = app.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Правый блок: название + рейтинг, краткое описание, категория
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // 2. Название и рейтинг
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (app.rating != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "★ ${app.rating}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFA000)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // 3. Краткое описание приложения
            Text(
                text = app.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Категория приложения
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = app.category.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    maxLines = 1
                )
            }
        }
    }
}

private val previewApps = listOf(
    AppItem(
        id = "1",
        name = "ВКонтакте",
        description = "Социальная сеть: мессенджер, звонки, музыка и сообщества",
        category = AppCategory.SOCIAL,
        iconRes = R.drawable.ic_launcher_foreground,
        rating = 4.7f
    ),
    AppItem(
        id = "2",
        name = "RuStore",
        description = "Официальный магазин мобильных приложений для Android",
        category = AppCategory.TOOLS,
        iconRes = R.drawable.ic_launcher_foreground,
        rating = 4.5f
    )
)

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AppListScreenPreview() {
    MaterialTheme {
        AppListScreen(
            uiState = AppListUiState(
                apps = previewApps,
                sequentialTimeMs = 15240L,
                parallelTimeMs = 1518L
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onAppClick = {},
            onToolbarClick = {},
            onLoadSequentialClick = {},
            onLoadParallelClick = {}
        )
    }
}

@Preview(name = "Single Item Preview", showBackground = true)
@Composable
private fun AppListItemPreview() {
    MaterialTheme {
        AppListItem(
            app = previewApps.first(),
            onAppClick = {}
        )
    }
}