package com.example.memory.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.memory.data.AppDatabase
import com.example.memory.data.Score
import com.example.memory.data.Settings
import com.example.memory.utils.MEMORY_DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(MEMORY_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val plantType = object : TypeToken<List<Score>>() {}.type
                    val scores: List<Score> = Gson().fromJson(jsonReader, plantType)

                    val plantType2 = object : TypeToken<List<Settings>>() {}.type
                    val settings: List<Settings> = Gson().fromJson(jsonReader, plantType2)

                    val database = AppDatabase.getInstance(applicationContext)
                    scores.forEach {
                        database.scoreDao().insert(it)
                    }
                    settings.forEach {
                        database.settingsDao().insert(it)
                    }
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
    }
}