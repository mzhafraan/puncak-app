package com.zhafran0006.puncak.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GearDao {
    @Query("SELECT * FROM gears WHERE mountainName = :mountainName AND isDeleted = 0")
    fun getGearsByMountain(mountainName: String): Flow<List<GearEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGear(gear: GearEntity)

    @Update
    suspend fun updateGear(gear: GearEntity)

    @Delete
    suspend fun deleteGear(gear: GearEntity)
    
    @Query("SELECT * FROM gears WHERE isDeleted = 1")
    fun getDeletedGears(): Flow<List<GearEntity>>
}
