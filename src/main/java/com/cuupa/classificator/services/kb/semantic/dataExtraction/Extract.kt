package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

abstract class Extract(var pattern: Pattern) {
    abstract fun normalize(value: String): String
}