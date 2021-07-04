package com.tsellami.evenly.repository.rooms.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venues")
data class Venue(
    @PrimaryKey val id: String,
    val name: String,
    val categoryName: String,
    val address: String,
    val distance: Int,
)
