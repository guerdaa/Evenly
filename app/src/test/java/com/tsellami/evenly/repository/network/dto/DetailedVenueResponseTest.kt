package com.tsellami.evenly.repository.network.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class DetailedVenueResponseTest {

    @Test
    fun `Assert that toEntity converts DetailedVenueResponse object to DetailedVenue object`() {
        val id = "ID"
        val url = "URL"
        val price = Price("MESSAGE")
        val rating = 1f
        val hours = OpeningHours("STATUS")
        val bestPhoto = Thumbnail("Prefix", "Suffix", 100, 100)
        val coordinates = Coordinates(60f, 50f)
        val details = Details(id, url, price, rating, hours, bestPhoto, coordinates)
        val venueDetails = VenueDetails(details)
        val response = DetailedVenueResponse(venueDetails)

        val result = response.toEntity()

        assertEquals(result.id, id)
        assertEquals(result.canonicalUrl, url)
        assertEquals(result.price, price.message)
        assertEquals(result.rating, rating)
        assertEquals(result.openingHours, hours.status)
        assertEquals(result.thumbnail, bestPhoto.url)
        assertEquals(result.lat, coordinates.lat)
        assertEquals(result.lng, coordinates.lng)
    }

    @Test
    fun `Assert that thumbnail url is correctly formatted`() {
        val prefix = "Prefix"
        val suffix = "Suffix"
        val width = 100
        val height = 150
        val bestPhoto = Thumbnail(prefix, suffix, width, height)

        val result = bestPhoto.url

        assertEquals(result, "$prefix${width}x${height}$suffix")
    }

    companion object {
        private const val ID = "ID"
        private const val URL = "URL"

    }
}