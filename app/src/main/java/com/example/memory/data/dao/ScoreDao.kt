package com.example.memory.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.memory.data.Score
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ScoreDao: BaseDao<Score> {

    @Transaction
    @Query("SELECT * FROM Score")
    abstract fun getAllScores(): Flow<List<Score>>

    @Transaction
    @Query("DELETE FROM Score")
    abstract fun clearScoreTable()
}