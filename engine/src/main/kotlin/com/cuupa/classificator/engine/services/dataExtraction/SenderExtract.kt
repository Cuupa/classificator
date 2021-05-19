package com.cuupa.classificator.engine.services.dataExtraction

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class SenderExtract(regex: String) : Extract(Regex(regex.trim(), RegexOption.IGNORE_CASE)) {

    override fun normalize(value: String): String {
        return value.trim()
    }

    companion object {
        const val name = "[SENDER]"
    }
}