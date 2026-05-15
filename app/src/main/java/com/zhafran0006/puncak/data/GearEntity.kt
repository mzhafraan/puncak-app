package com.zhafran0006.puncak.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gears")
data class GearEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mountainName: String,
    val gearName: String,
    val isPacked: Boolean = false,
    val isDeleted: Boolean = false // For Recycle Bin (h)
)
