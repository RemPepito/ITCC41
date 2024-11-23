package com.example.itcc41.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TabUsageDao {
    @Insert
    suspend fun insertTabUsage(tabUsage: TabUsage)

    @Query("SELECT * FROM tab_usage ORDER BY timestamp DESC")
    suspend fun getAllTabUsage(): List<TabUsage>
}
