package com.tsellami.evenly.repository.network.dto

data class RecommendationResponse(
    val response: Groups
)

data class Groups(
    val groups: List<Items>
)

data class Items(
    val items: List<Recommendation>
)

data class Recommendation(
    val venue: VenueDto
)

data class VenueDto(
    val id: String,
    val name: String,
    val categories: List<Category>,
    val location: Location
) {
    val formattedLocation get() = location.formattedAddress.joinToString(separator = ", ")
}

data class Location(
    val formattedAddress: List<String>,
    val distance: Int
)

data class Category(
    val name: String
)
