package com.pelagohealth.codingchallenge.presentation.model

sealed class Message {
    data object GeneralError : Message()
    data object FactDeleted : Message()
}
