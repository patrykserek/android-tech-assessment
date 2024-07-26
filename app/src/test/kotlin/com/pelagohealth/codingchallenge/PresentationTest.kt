package com.pelagohealth.codingchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class PresentationTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()
    private val coroutineDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUpMainDispatcher() {
        Dispatchers.setMain(coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
