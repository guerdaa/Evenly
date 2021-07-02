package com.tsellami.evenly.network.recommendations

import android.util.Log
import com.tsellami.evenly.BuildConfig
import com.tsellami.evenly.rooms.DetailedVenue
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApi {

    @GET("explore")
    suspend fun getRecommendations(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
        @Query("v") v: String = BuildConfig.V,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int,
        @Query("ll") location: String = BuildConfig.LOCATION
    ): RecommendationResponse

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
        val venue: Venue
    )

    data class Venue(
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

    //------------------------

    @GET("{id}")
    suspend fun getDetailedVenue(
        @Path("id") id: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
        @Query("v") v: String = BuildConfig.V,
    ): DetailedVenueResponse

    data class DetailedVenueResponse(val response: VenueDetails) {
        fun toEntity(): DetailedVenue {
            Log.d("∞∞∞∞∞∞∞∞∞", response.venue.toString())
            response.venue.apply {
                return DetailedVenue(
                    id,
                    price?.message ?: "Not available",
                    rating,
                    hours?.status ?: "Not available",
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
        val height: String
    ) {
        val url get() = "$prefix${width}x${height}$suffix"
    }

    data class Coordinates(
        val lat: Float,
        val lng: Float
    )
    companion object {
        const val BASE_URL = "https://api.foursquare.com/v2/venues/"
    }
}