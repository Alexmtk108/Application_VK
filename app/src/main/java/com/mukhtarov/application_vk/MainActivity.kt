package com.mukhtarov.application_vk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mukhtarov.application_vk.ui.theme.Application_VKTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etInput = findViewById<EditText>(R.id.etInput)
        val btnOpenSecond = findViewById<Button>(R.id.btnOpenSecond)
        val btnCallFriend = findViewById<Button>(R.id.btnCallFriend)
        val btnShareText = findViewById<Button>(R.id.btnShareText)

        btnOpenSecond.setOnClickListener {
            val inputText = etInput.text.toString().trim()


            if (inputText.isEmpty()) {
                etInput.error = "Поле не должно быть пустым"
            } else {
                val intent = Intent(this, SecondActivity::class.java).apply {
                    putExtra("EXTRA_TEXT", inputText)
                }
                startActivity(intent)
            }
        }

        btnCallFriend.setOnClickListener {
            val phoneNumber = etInput.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                etInput.error = "Введите номер телефона"
            } else {
                // ACTION_DIAL открывает звонилку
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(dialIntent)
            }
        }

        btnShareText.setOnClickListener {
            val textToShare = etInput.text.toString().trim()

            if (textToShare.isEmpty()) {
                etInput.error = "Введите текст, чтобы поделиться"
            } else {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, textToShare)
                }

                val chooser = Intent.createChooser(shareIntent, "Поделиться через...")
                startActivity(chooser)
            }
        }
    }
}
