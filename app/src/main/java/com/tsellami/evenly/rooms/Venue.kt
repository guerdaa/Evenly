package com.tsellami.evenly.rooms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venues")
data class Venue(
    @PrimaryKey val id: String,
    val name: String,
    val categoryName: String,
    val address: String,
    val distance: Int,
    val isFavorite: Boolean
)
