package com.tsellami.evenly.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.tsellami.evenly.repository.VenuesRepository
import com.tsellami.evenly.rooms.Venue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: VenuesRepository
): ViewModel() {

    val favorites: LiveData<PagingData<Venue>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100
        )
    ) {
        repository.retrieveFavorites()
    }.liveData.cachedIn(viewModelScope)

}