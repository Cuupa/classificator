package com.cuupa.classificator.api_implementation.v1

import com.cuupa.classificator.api.v1.PingApi
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@RestController
@Deprecated(message = "Deprecated for API version 2")
open class PingApiController: PingApi {

    override fun ping(request: HttpServletRequest?): ResponseEntity<String> {
        log.error("Endpoint '/api/rest/1.0/ping' was called from '${request?.requestURI ?: "unknown"}'")
        val headers = HttpHeaders().apply {
            set("Warning", "Deprecated")
            set("Deprecation", "true")
            contentType = MediaType.TEXT_PLAIN
        }
        return ResponseEntity.ok().headers(headers).body("")
    }

    companion object{
        private val log = LogFactory.getLog(PingApiController::class.java)
    }
}