package com.cuupa.classificator.services.kb.semantic.token

class InvalidTokenException : RuntimeException {
    constructor(string: String?) : super(string) {}
    constructor() {}

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1L
    }
}