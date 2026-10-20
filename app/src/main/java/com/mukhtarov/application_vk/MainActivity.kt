package com.mukhtarov.application_vk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mukhtarov.application_vk.ui.AppListScreen
import com.mukhtarov.application_vk.ui.theme.Application_VKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Application_VKTheme {
                AppListScreen()
            }
        }
    }
}
