package com.example.memory.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.memory.data.Settings
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SettingsDao: BaseDao<Settings> {

    @Query("SELECT * FROM Settings ORDER BY uid DESC LIMIT 1")
    abstract fun getSettings(): Flow<Settings>

    @Transaction
    @Query("DELETE FROM Settings")
    abstract fun deletePreviousSettings()
}