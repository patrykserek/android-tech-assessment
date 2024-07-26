package com.pelagohealth.codingchallenge

import com.pelagohealth.codingchallenge.data.datasource.rest.APIFact
import com.pelagohealth.codingchallenge.data.datasource.rest.FactsRestApi
import java.util.UUID

class FakeFactsRestApi : FactsRestApi {

    var facts = listOf(
        "Each month, there is at least one report of UFOs from each province of Canada.",
        "Right-handed people live, on average; nine years longer than left handed people.",
        "100% of lottery winners do gain weight",
        "The citrus soda 7-UP was created in 1929; `7` was selected after the original 7-ounce containers and `UP` for the direction of the bubbles.",
        "Every acre of American crops harvested contains 100 pounds of insects.",
    )

    override suspend fun getFact(): APIFact {
        val fact = facts.last()
        facts = facts.dropLast(1)

        return APIFact(
            id = UUID.randomUUID().toString(),
            text = fact,
            sourceUrl = "https://uselessfacts.jsph.pl/random/example-id"
        )
    }
}
