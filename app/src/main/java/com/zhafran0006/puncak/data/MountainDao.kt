package com.zhafran0006.puncak.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MountainDao {
    @Query("SELECT * FROM mountains")
    fun getAllMountains(): Flow<List<MountainEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMountain(mountain: MountainEntity)

    @Query("SELECT COUNT(*) FROM mountains")
    suspend fun getMountainCount(): Int
}
