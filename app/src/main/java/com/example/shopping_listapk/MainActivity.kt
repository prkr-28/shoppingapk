package com.example.shopping_listapk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.shopping_listapk.ui.theme.Shopping_listApkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Shopping_listApkTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                        color= Color(0xFFc5eab3)
                ) {
                    ShoppingList()
                }
            }
        }
    }
}