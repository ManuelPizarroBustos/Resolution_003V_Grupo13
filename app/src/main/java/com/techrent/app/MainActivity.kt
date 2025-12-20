package com.techrent.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.techrent.app.ui.navigation.AppNavGraph
import com.techrent.app.ui.theme.TechRentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechRentTheme {
                AppNavGraph()
            }
        }
    }
}
