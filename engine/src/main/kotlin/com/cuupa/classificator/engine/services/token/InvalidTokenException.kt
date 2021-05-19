package com.cuupa.classificator.engine.services.token

class InvalidTokenException(string: String?) : RuntimeException(string) {

    companion object {
        private const val serialVersionUID = 1L
    }
}