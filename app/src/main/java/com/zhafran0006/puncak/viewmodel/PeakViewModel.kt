package com.zhafran0006.puncak.viewmodel

import androidx.lifecycle.ViewModel
import com.zhafran0006.puncak.screen.Gear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PeakViewModel : ViewModel() {
    private val _allGearLists = MutableStateFlow<Map<String, List<Gear>>>(emptyMap())

    // Tambahin baris ini buat diekspos ke UI biar bisa dipantau real-time
    val allGearLists = _allGearLists.asStateFlow()

    // Fungsi getGearList HAPUS aja, udah nggak kepakai

    fun addGear(mountainName: String, gearName: String) {
        val currentMap = _allGearLists.value.toMutableMap()
        val currentList = currentMap[mountainName]?.toMutableList() ?: mutableListOf()
        currentList.add(Gear(gearName))
        currentMap[mountainName] = currentList
        _allGearLists.value = currentMap
    }

    fun toggleGear(mountainName: String, gear: Gear) {
        val currentMap = _allGearLists.value.toMutableMap()
        val currentList = currentMap[mountainName]?.map {
            if (it.name == gear.name) it.copy(isPacked = !it.isPacked) else it
        } ?: emptyList()
        currentMap[mountainName] = currentList
        _allGearLists.value = currentMap
    }
}