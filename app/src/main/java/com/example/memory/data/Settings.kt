package com.example.memory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings (
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    val theme: String = "Logos",
    val difficulty: String = "Normal"
) {

}
