package com.pelagohealth.codingchallenge.presentation

import com.pelagohealth.codingchallenge.FakeFactsRestApi
import com.pelagohealth.codingchallenge.PresentationTest
import com.pelagohealth.codingchallenge.TestDispatcherProvider
import com.pelagohealth.codingchallenge.data.repository.FactRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainViewModelTest : PresentationTest() {

    private lateinit var systemUnderTest: MainViewModel

    @BeforeTest
    fun setup() {
        val repository = FactRepository(
            FakeFactsRestApi(),
            TestDispatcherProvider(),
        )
        systemUnderTest = MainViewModel(repository)
    }

    @Test
    fun `when init then fetch new fact`() {
        assertEquals(
            expected = "Every acre of American crops harvested contains 100 pounds of insects.",
            actual = systemUnderTest.state.value.latestFact,
        )
    }

    @Test
    fun `when init then previous facts are empty`() {
        assertTrue(systemUnderTest.state.value.previousFacts.isEmpty())
    }

    @Test
    fun `when more facts clicked then fetch new fact`() {
        systemUnderTest.onMoreFactsClicked()

        assertEquals(
            expected = "The citrus soda 7-UP was created in 1929; `7` was selected after the original 7-ounce containers and `UP` for the direction of the bubbles.",
            actual = systemUnderTest.state.value.latestFact,
        )
    }

    @Test
    fun `when more facts clicked then update previous facts`() {
        systemUnderTest.onMoreFactsClicked()

        assertEquals(
            expected = listOf("Every acre of American crops harvested contains 100 pounds of insects."),
            actual = systemUnderTest.state.value.previousFacts,
        )
    }

    @Test
    fun `when more facts clicked couple times then keep max 3 previous facts`() {
        repeat(4) {
            systemUnderTest.onMoreFactsClicked()
        }

        assertEquals(
            expected = listOf(
                "The citrus soda 7-UP was created in 1929; `7` was selected after the original 7-ounce containers and `UP` for the direction of the bubbles.",
                "100% of lottery winners do gain weight",
                "Right-handed people live, on average; nine years longer than left handed people.",
            ),
            actual = systemUnderTest.state.value.previousFacts,
        )
    }

    @Test
    fun `when previous fact swiped then remove it and notify`() {
        val expectedFact = "Every acre of American crops harvested contains 100 pounds of insects."

        systemUnderTest.onMoreFactsClicked() // add fact to previous
        assertContains(
            iterable = systemUnderTest.state.value.previousFacts,
            element = expectedFact
        )

        systemUnderTest.onFactSwiped(expectedFact)
        assertTrue(systemUnderTest.state.value.previousFacts.isEmpty())
    }
}
