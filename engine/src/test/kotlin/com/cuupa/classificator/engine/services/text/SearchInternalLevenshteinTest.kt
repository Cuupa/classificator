package com.cuupa.classificator.engine.services.text

import org.junit.jupiter.api.Test

class SearchInternalLevenshteinTest {

    @Test
    fun test() {
        var unitToTest = SearchInternalLevenshtein(
            SearchText("lorem ipsum"),
            PlainText("asduhasd lorem dashd lorem lorem sauhsdfasdf lorem ipsum "),
            1
        )
        while (unitToTest.isToSearch) {
            unitToTest = unitToTest.invoke()
        }
        print(unitToTest.found())
    }
}