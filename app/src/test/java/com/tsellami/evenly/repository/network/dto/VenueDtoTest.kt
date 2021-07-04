package com.tsellami.evenly.repository.network.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class VenueDtoTest {

    @Test
    fun `Assert that formattedLocation returns a correct format of location`() {
        val location = Location(
            listOf("1", "2", "3", "4"),
            1
        )
        val venueDto = VenueDto("ID", "NAME", emptyList(), location)

        val result = venueDto.formattedLocation

        assertEquals(result, "1, 2, 3, 4")
    }
}