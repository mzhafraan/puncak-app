package com.zhafran0006.puncak.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zhafran0006.puncak.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PeakViewModel(
    private val gearDao: GearDao,
    private val mountainDao: MountainDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // --- MOUNTAINS ---
    val allMountains: StateFlow<List<MountainEntity>> = mountainDao.getAllMountains()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            if (mountainDao.getMountainCount() == 0) {
                mountainDao.insertMountain(MountainEntity(name = "Gunung Ciremai", elevation = "3.078 MDPL", location = "Via Apuy"))
                mountainDao.insertMountain(MountainEntity(name = "Gunung Pangradinan", elevation = "1.234 MDPL", location = "Jawa Barat"))
                mountainDao.insertMountain(MountainEntity(name = "Gunung Gede", elevation = "2.958 MDPL", location = "Cibodas"))
                mountainDao.insertMountain(MountainEntity(name = "Gunung Salak", elevation = "2.211 MDPL", location = "Cidahu"))
            }
        }
    }

    // --- GEARS ---
    fun getGearsForMountain(mountainName: String): Flow<List<GearEntity>> {
        return gearDao.getGearsByMountain(mountainName)
    }

    fun addGear(mountainName: String, gearName: String) {
        if (gearName.isBlank()) return
        viewModelScope.launch {
            val newGear = GearEntity(
                mountainName = mountainName,
                gearName = gearName.trim(),
                isPacked = false
            )
            gearDao.insertGear(newGear)
        }
    }

    fun toggleGear(gear: GearEntity) {
        viewModelScope.launch {
            gearDao.updateGear(gear.copy(isPacked = !gear.isPacked))
        }
    }

    fun updateGearName(gear: GearEntity, newName: String) {
        viewModelScope.launch {
            gearDao.updateGear(gear.copy(gearName = newName))
        }
    }

    fun moveToRecycleBin(gear: GearEntity) {
        viewModelScope.launch {
            gearDao.updateGear(gear.copy(isDeleted = true))
        }
    }

    // --- PREFERENCES (Requirement f, i) ---
    val isDarkMode: StateFlow<Boolean> = userPreferencesRepository.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isGridLayout: StateFlow<Boolean> = userPreferencesRepository.isGridLayout
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val themeColorIndex: StateFlow<Int> = userPreferencesRepository.themeColorIndex
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun toggleTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveDarkModePreference(isDarkMode)
        }
    }

    fun toggleLayout(isGridLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isGridLayout)
        }
    }

    fun setThemeColor(index: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveThemeColorPreference(index)
        }
    }
}

class PeakViewModelFactory(
    private val gearDao: GearDao,
    private val mountainDao: MountainDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeakViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeakViewModel(gearDao, mountainDao, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
