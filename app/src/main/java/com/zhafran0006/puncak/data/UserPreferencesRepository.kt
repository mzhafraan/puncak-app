package com.zhafran0006.puncak.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_GRID_LAYOUT = booleanPreferencesKey("is_grid_layout")
        val THEME_COLOR_INDEX = intPreferencesKey("theme_color_index")
    }

    val isDarkMode: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[PreferencesKeys.IS_DARK_MODE] ?: false }

    val isGridLayout: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[PreferencesKeys.IS_GRID_LAYOUT] ?: false }

    val themeColorIndex: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[PreferencesKeys.THEME_COLOR_INDEX] ?: 0 }

    suspend fun saveDarkModePreference(isDarkMode: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode }
    }

    suspend fun saveLayoutPreference(isGridLayout: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.IS_GRID_LAYOUT] = isGridLayout }
    }

    suspend fun saveThemeColorPreference(index: Int) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.THEME_COLOR_INDEX] = index }
    }
}
