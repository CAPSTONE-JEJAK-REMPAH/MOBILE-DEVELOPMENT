package com.example.jejakrempah.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = true)
    val no: Int = 0,  // Primary key
    val rempah: String,
    val imageUrl: String,
    val description: String
)

