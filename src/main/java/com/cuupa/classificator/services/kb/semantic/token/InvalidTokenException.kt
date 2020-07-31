package com.cuupa.classificator.services.kb.semantic.token

class InvalidTokenException(string: String?) : RuntimeException(string) {

    companion object {
        private const val serialVersionUID = 1L
    }
}