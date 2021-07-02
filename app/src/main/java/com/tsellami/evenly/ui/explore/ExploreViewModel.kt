package com.tsellami.evenly.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tsellami.evenly.repository.VenuesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: VenuesRepository
) : ViewModel() {

    val venues = repository.getRecommendationsResultPaged().cachedIn(viewModelScope).asLiveData()

    fun deleteCachedData() {
        viewModelScope.launch {
            repository.deleteAllRecommendations()
        }
    }

}