package com.pelagohealth.codingchallenge.data.repository

import com.pelagohealth.codingchallenge.data.datasource.rest.FactsRestApi
import com.pelagohealth.codingchallenge.data.mapper.toFact
import com.pelagohealth.codingchallenge.domain.dispatcher.DispatcherProvider
import com.pelagohealth.codingchallenge.domain.model.Fact
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository providing random facts.
 */
class FactRepository @Inject constructor(
    private val factsRestApi: FactsRestApi,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun get(): Fact = withContext(dispatcherProvider.io) {
        factsRestApi.getFact().toFact()
    }

}