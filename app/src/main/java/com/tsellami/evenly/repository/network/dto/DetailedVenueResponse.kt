package com.tsellami.evenly.repository.network.dto

import com.tsellami.evenly.repository.rooms.models.DetailedVenue

data class DetailedVenueResponse(val response: VenueDetails) {
    fun toEntity(): DetailedVenue {
        response.venue.apply {
            return DetailedVenue(
                id,
                price?.message,
                rating,
                hours?.status,
                canonicalUrl,
                bestPhoto.url,
                location.lat,
                location.lng
            )
        }
    }
}

data class VenueDetails(val venue: Details)

data class Details(
    val id: String,
    val canonicalUrl: String,
    val price: Price?,
    val rating: Float,
    val hours: OpeningHours?,
    val bestPhoto: Thumbnail,
    val location: Coordinates
)

data class OpeningHours(val status: String)

data class Price(val message: String)

data class Thumbnail(
    val prefix: String,
    val suffix: String,
    val width: Int,
    val height: Int
) {
    val url get() = "$prefix${width}x${height}$suffix"
}

data class Coordinates(
    val lat: Float,
    val lng: Float
)