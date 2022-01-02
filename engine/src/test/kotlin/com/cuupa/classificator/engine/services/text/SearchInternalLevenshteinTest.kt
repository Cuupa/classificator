package com.cuupa.classificator.engine.services.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SearchInternalLevenshteinTest {

    @Test
    fun shouldFindPhrase() {
        var unitToTest = SearchInternalLevenshtein(
            SearchText("lorem ipsum"),
            PlainText("asduhasd lorem dashd lorem lorem sauhsdfasdf lorem ipsum "),
            1
        )
        while (unitToTest.isToSearch) {
            unitToTest = unitToTest.invoke()
        }
        assertTrue(unitToTest.found)
    }

    @Test
    fun shouldCoundPhrase() {
        var unitToTest = SearchInternalLevenshtein(
            SearchText("lorem"),
            PlainText("asduhasd lorem dashd lorem lorem sauhsdfasdf lorem ipsum "),
            1
        )
        while (unitToTest.isToSearch) {
            unitToTest = unitToTest.invoke()
        }
        assertEquals(4, unitToTest.numberOfOccurrences)
    }
}