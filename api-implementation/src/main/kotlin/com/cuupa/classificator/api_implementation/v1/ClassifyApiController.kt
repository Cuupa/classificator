package com.cuupa.classificator.api_implementation.v1

import com.cuupa.classificator.api.v1.ClassifyApi
import com.cuupa.classificator.engine.ClassificatorOld
import com.google.gson.GsonBuilder
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@RestController
@Deprecated(message = "Deprecated for API version 2")
open class ClassifyApiController
@Autowired constructor(
    private val classificator: ClassificatorOld
) : ClassifyApi {

    private val headers = HttpHeaders().apply {
        set("script-src", "self")
        set("Warning", "Deprecated")
        set("Deprecation", "true")
        contentType = MediaType.APPLICATION_JSON
    }

    override fun classify(text: String?, request: HttpServletRequest?): ResponseEntity<String> {
        return invoke(request, text, "/api/rest/1.0/classifyText")
    }

    override fun classify(content: ByteArray?, request: HttpServletRequest?): ResponseEntity<String> {
        return invoke(request, content, "/api/rest/1.0/classify")
    }

    private fun invoke(
        request: HttpServletRequest?,
        content: Any?,
        url: String
    ): ResponseEntity<String> {
        log.warn("Endpoint '${url}' was called from '${request?.requestURI ?: "unknown"}'")
        try {
            val result = when (content) {
                is ByteArray -> classificator.classify(content)
                is String -> classificator.classify(content)
                else -> listOf()
            }
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(gson.toJson(result))
        } catch (e: Exception) {
            log.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(null)
    }

    companion object {
        private val gson = GsonBuilder().serializeNulls().create()
        private val log: Log = LogFactory.getLog(ClassifyApiController::class.java)
    }
}