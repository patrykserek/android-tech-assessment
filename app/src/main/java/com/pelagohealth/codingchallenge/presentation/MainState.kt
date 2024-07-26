package com.pelagohealth.codingchallenge.presentation

data class MainState(
    val latestFact: String = "",
    val previousFacts: List<String> = emptyList(),
)
