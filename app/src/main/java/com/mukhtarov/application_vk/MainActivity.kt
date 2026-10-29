package com.mukhtarov.application_vk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mukhtarov.application_vk.ui.AppNavigation
import com.mukhtarov.application_vk.presentation.ui.theme.Application_VKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Application_VKTheme {
                AppNavigation()
            }
        }
    }
}
