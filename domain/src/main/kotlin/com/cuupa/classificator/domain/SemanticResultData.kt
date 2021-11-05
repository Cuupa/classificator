package com.cuupa.classificator.domain

abstract class SemanticResultData(var name: String = "") {

    abstract fun addToken(token: Token)
}
