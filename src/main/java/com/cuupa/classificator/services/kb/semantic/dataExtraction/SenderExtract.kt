package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class SenderExtract(regex: String) : Extract(Pattern.compile(regex.trim(), Pattern.CASE_INSENSITIVE)) {

    override fun normalize(value: String): String {
        return value.trim()
    }

    companion object {
        const val name = "[SENDER]"
    }
}