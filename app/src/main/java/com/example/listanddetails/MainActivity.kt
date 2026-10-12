package com.example.listanddetails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.listanddetails.ui.navigation.AppNavHost
import com.example.listanddetails.ui.theme.ListAndDetailsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListAndDetailsTheme {
                AppNavHost(onExitApp = ::finish)
            }
        }
    }
}