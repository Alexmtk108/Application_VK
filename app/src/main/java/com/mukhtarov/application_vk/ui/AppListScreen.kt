package com.mukhtarov.application_vk.ui

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mukhtarov.application_vk.R
import com.mukhtarov.application_vk.model.AppItem
import com.mukhtarov.application_vk.model.MockAppsData
import com.mukhtarov.application_vk.ui.viewmodel.AppListViewModel

/**
 * Основной экран со списком приложений.
 * Компоновка сверху вниз: Тулбар -> Список приложений.
 */

/**
 * Stateful-обертка, которая связывает ViewModel с UI.
 */
@Composable
fun AppListRoute(
    onAppClick: (Int) -> Unit,
    viewModel: AppListViewModel = viewModel()
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
        apps = uiState.apps,
        snackbarHostState = snackbarHostState,
        onAppClick = onAppClick,
        onToolbarClick = { viewModel.onToolbarClicked() }
    )
}

/**
 * Stateless-экран (только верстка, никаких данных и логики).
 */
@Composable
fun AppListScreen(
    apps: List<AppItem>,
    snackbarHostState: SnackbarHostState,
    onAppClick: (Int) -> Unit,
    onToolbarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppListToolbar(onToolbarClick = onToolbarClick)
        }
    ) { innerPadding ->
        AppList(
            apps = apps,
            modifier = Modifier.padding(innerPadding),
            onAppClick = onAppClick
        )
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
    onAppClick: (Int) -> Unit = {},
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
    onAppClick: (Int) -> Unit,
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

        // Правый блок: название, краткое описание, категория
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // 2. Название приложения
            Text(
                text = app.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

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
            apps = MockAppsData.appsList, // Передаем фейковые данные для отрисовки
            snackbarHostState = remember { SnackbarHostState() }, // Пустой стейт
            onAppClick = {},
            onToolbarClick = {})
    }
}

@Preview(name = "Single Item Preview", showBackground = true)
@Composable
private fun AppListItemPreview() {
    MaterialTheme {
        AppListItem(
            app = MockAppsData.appsList.first(),
            onAppClick = {}
        )
    }
}