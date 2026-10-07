package com.mukhtarov.application_vk

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tvDisplay = findViewById<TextView>(R.id.tvDisplay)

        val receivedText = intent.getStringExtra("EXTRA_TEXT")

        tvDisplay.text = receivedText ?: "Нет данных"
    }
}