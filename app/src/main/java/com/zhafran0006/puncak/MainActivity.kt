package com.zhafran0006.puncak

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhafran0006.puncak.data.AppDatabase
import com.zhafran0006.puncak.data.UserPreferencesRepository
import com.zhafran0006.puncak.screen.AboutScreen
import com.zhafran0006.puncak.screen.DetailScreen
import com.zhafran0006.puncak.screen.HomeScreen
import com.zhafran0006.puncak.ui.theme.PuncakTheme
import com.zhafran0006.puncak.viewmodel.PeakViewModel
import com.zhafran0006.puncak.viewmodel.PeakViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val userPreferencesRepository = UserPreferencesRepository(dataStore)
        val factory = PeakViewModelFactory(
            database.gearDao(),
            database.mountainDao(),
            userPreferencesRepository
        )

        setContent {
            val peakViewModel: PeakViewModel = viewModel(factory = factory)
            val isDarkMode by peakViewModel.isDarkMode.collectAsState()
            val themeIndex by peakViewModel.themeColorIndex.collectAsState()

            PuncakTheme(darkTheme = isDarkMode, themeIndex = themeIndex) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = peakViewModel,
                            onNavigate = { mountainName ->
                                navController.navigate("detail/$mountainName")
                            },
                            onAboutNavigate = {
                                navController.navigate("about")
                            }
                        )
                    }
                    composable("detail/{mountainName}") { backStackEntry ->
                        val mountainName = backStackEntry.arguments?.getString("mountainName") ?: ""
                        DetailScreen(
                            mountainName = mountainName,
                            viewModel = peakViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("about") {
                        AboutScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
