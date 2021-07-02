package com.tsellami.evenly.rooms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteVenue(
    @PrimaryKey val id: String
)