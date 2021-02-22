package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

abstract class Extract(val pattern: Pattern) {
    abstract fun normalize(value: String): String
}