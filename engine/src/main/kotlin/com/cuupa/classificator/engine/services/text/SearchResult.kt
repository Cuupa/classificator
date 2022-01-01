package com.cuupa.classificator.engine.services.text

data class SearchResult(val contains: Boolean = false, val distance: Int = 0, val numberOfOccurences: Int = 0)
