package com.tsellami.evenly.repository

import com.tsellami.evenly.repository.network.FoursquareApi
import com.tsellami.evenly.repository.rooms.VenuesDatabase
import io.mockk.mockk
import org.junit.Before

open class BaseRepositoryTest {

    protected val venuesDatabase: VenuesDatabase = mockk(relaxed = true)
    protected val foursquareApi: FoursquareApi = mockk(relaxed = true)


    companion object {
        const val ID = "ID"
    }
}