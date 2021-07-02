package com.tsellami.evenly.utils

sealed class Resource {
    object Loading: Resource()
    object Success: Resource()
}