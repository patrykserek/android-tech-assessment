package com.pelagohealth.codingchallenge.data.mapper

import com.pelagohealth.codingchallenge.data.datasource.rest.APIFact
import com.pelagohealth.codingchallenge.domain.model.Fact

fun APIFact.toFact() = Fact(
    text = text,
    url = sourceUrl,
)
