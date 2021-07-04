package com.tsellami.evenly.repository.rooms.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detailed_venues")
data class DetailedVenue(
    @PrimaryKey val id: String,
    val price: String?,
    val rating: Float,
    val openingHours: String?,
    val canonicalUrl: String,
    val thumbnail: String,
    val lat: Float,
    val lng: Float
)