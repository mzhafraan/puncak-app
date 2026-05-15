package com.zhafran0006.puncak.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mountains")
data class MountainEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val elevation: String,
    val location: String,
    val description: String = ""
)
