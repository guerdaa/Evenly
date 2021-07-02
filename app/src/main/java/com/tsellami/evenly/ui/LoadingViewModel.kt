package com.tsellami.evenly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsellami.evenly.utils.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class LoadingViewModel: ViewModel() {

    protected val channel = Channel<Resource>()
    val resource = channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            channel.send(Resource.Loading)
        }
    }
}