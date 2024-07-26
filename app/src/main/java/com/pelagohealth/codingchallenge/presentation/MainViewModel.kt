package com.pelagohealth.codingchallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pelagohealth.codingchallenge.data.repository.FactRepository
import com.pelagohealth.codingchallenge.presentation.model.MainState
import com.pelagohealth.codingchallenge.presentation.model.Message
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

    fun onMoreFactsClicked() {
        fetchNewFact()
    }

    fun onFactSwiped(fact: String) {
        _state.update { oldState ->
            oldState.copy(
                previousFacts = oldState.previousFacts.filter { it != fact },
                message = Message.FactDeleted,
            )
        }
    }

    fun onMessageDismissed() {
        _state.update { it.copy(message = null) }
    }

    private fun fetchNewFact() {
        viewModelScope.launch {
            updateHistory()
            runCatching { repository.get() }
                .onSuccess { updateLatestFact(it.text) }
                .onFailure {
                    _state.update {
                        it.copy(message = Message.GeneralError)
                    }
                }
        }
    }

    private fun updateHistory() {
        _state.update { oldState ->
            val previousFacts = (oldState.previousFacts + oldState.latestFact)
                .filter { it.isNotBlank() }
                .takeLast(3)

            oldState.copy(latestFact = "", previousFacts = previousFacts)
        }
    }

    private fun updateLatestFact(fact: String) {
        _state.update {
            it.copy(latestFact = fact)
        }
    }
}
