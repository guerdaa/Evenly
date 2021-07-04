package com.tsellami.evenly.repository.rooms.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteVenue(
    @PrimaryKey val id: String
)