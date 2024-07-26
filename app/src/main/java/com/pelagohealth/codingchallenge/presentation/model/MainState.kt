package com.pelagohealth.codingchallenge.presentation.model

data class MainState(
    val latestFact: String = "",
    val previousFacts: List<String> = emptyList(),
    val message: Message? = null
)
