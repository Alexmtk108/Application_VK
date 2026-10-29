package com.mukhtarov.application_vk.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mukhtarov.application_vk.data.source.MockAppsDataSource
import com.mukhtarov.application_vk.domain.model.AppItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowcaseBlock(
    apps: List<AppItem>,
    onShowcaseAppClick: (Int) -> Unit, // Обработчик клика
    modifier: Modifier = Modifier
) {
    // Берем ровно 9 приложений из писка и разбиваем на группы по 3
    val showcasePages = remember(apps) {
        apps.take(9).chunked(3)
    }

    // Состояние пейджера с фиксированным количеством страниц
    val pagerState = rememberPagerState(pageCount = { showcasePages.size })

    Column(modifier = modifier.fillMaxWidth()) {
        // Заголовок витрины
        Text(
            text = "Витрина",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        // Горизонтальный свайпаемый Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { pageIndex ->

            // Колонка из 3-х элементов на текущей странице
            Column(modifier = Modifier.fillMaxWidth()) {
                val appsOnPage = showcasePages[pageIndex]

                appsOnPage.forEach { app ->
                    AppListItem(
                        app = app,
                        onAppClick = onShowcaseAppClick, // Передаем обработчик витрины напрямую
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
