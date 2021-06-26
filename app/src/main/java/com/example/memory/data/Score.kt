package com.example.memory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score (
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var points: Long = 0,
    var time: Long = 0,
    var difficulty: String,
    var date: String
) {
    fun convToString(el: Long): String {
        return el.toString()
    }

    fun convToTime(t: Long): String {
        val mins = t / 1000/ 60
        val secs = t / 1000 % 60

        if (mins == 0L)
            return secs.toString() + " s"

        return mins.toString() + " min " + secs.toString() + " s"
    }
}