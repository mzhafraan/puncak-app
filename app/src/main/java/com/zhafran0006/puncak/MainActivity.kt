package com.zhafran0006.puncak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhafran0006.puncak.screen.DetailScreen
import com.zhafran0006.puncak.screen.HomeScreen
import com.zhafran0006.puncak.ui.theme.PuncakTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Theme state yang di-handle otomatis oleh sistem (Dark/Light mode)
            PuncakTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(onNavigate = { mountainName ->
                            navController.navigate("detail/$mountainName")
                        })
                    }
                    composable("detail/{mountainName}") { backStackEntry ->
                        val mountainName = backStackEntry.arguments?.getString("mountainName") ?: ""
                        DetailScreen(mountainName = mountainName, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}