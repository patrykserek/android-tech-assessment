package com.pelagohealth.codingchallenge.presentation

data class MainState(
    val latestFact: String? = null,
    val previousFacts: List<String> = emptyList(),
)
