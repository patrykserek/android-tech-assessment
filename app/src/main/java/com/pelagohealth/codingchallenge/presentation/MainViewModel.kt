package com.pelagohealth.codingchallenge.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pelagohealth.codingchallenge.data.repository.FactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FactRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        fetchNewFact()
    }

    fun onFactSwiped(fact: String) {
        _state.update { oldState ->
            oldState.copy(
                previousFacts = oldState.previousFacts.filter { it != fact },
            )
        }
    }

    fun fetchNewFact() {
        viewModelScope.launch {
            runCatching { repository.get() }
                .onSuccess { updateState(it.text) }
                .onFailure { Log.w("PelagoApp", it) }
        }
    }

    private fun updateState(fact: String) {
        _state.update { oldState ->
            val previousFacts = (oldState.previousFacts + oldState.latestFact)
                .filterNotNull()
                .takeLast(3)

            oldState.copy(latestFact = fact, previousFacts = previousFacts)
        }
    }
}
