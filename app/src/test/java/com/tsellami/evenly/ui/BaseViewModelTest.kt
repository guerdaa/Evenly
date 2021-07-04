package com.tsellami.evenly.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tsellami.evenly.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

open class BaseViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
}