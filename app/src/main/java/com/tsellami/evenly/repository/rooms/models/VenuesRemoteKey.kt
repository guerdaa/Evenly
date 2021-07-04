package com.tsellami.evenly.repository.rooms.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venues_remote_keys")
data class VenuesRemoteKey(
    @PrimaryKey val location: String = "evenly",
    val nextPageKey: Int
)