package com.example.memory.data

import com.example.memory.data.dao.ScoreDao
import com.example.memory.data.dao.SettingsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    val scoreDao: ScoreDao,
    val settingsDao: SettingsDao
){
    fun getAllScores() = scoreDao.getAllScores()

    fun clearScoreTable() = scoreDao.clearScoreTable()

    fun getSettings() = settingsDao.getSettings()

    fun deletePreviousSettings() = settingsDao.deletePreviousSettings()
}