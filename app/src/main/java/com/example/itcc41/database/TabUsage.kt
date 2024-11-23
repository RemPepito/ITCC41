package com.example.itcc41.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tab_usage")
data class TabUsage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tabName: String,
    val timestamp: Long
)
