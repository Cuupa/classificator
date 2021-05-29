package com.cuupa.classificator.api.v1

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.engine.Classificator
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class ClassificatorController(private val classificator: Classificator) {

    private val headers = HttpHeaders().apply {
        set("script-src", "self")
        set("Warning", "Deprecated")
        set("Deprecation", "true")
        contentType = MediaType.APPLICATION_JSON
    }

    @Operation(summary = "Retrieves the application status", description = "", tags = ["health-check", "v1"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "The status of the application",
            content = [Content(schema = Schema(implementation = String::class))]
        )]
    )
    @RequestMapping(value = ["/api/rest/1.0/ping"], produces = ["text/plain"], method = [RequestMethod.GET])
    fun ping(request: HttpServletRequest?): ResponseEntity<String> {
        log.error("Endpoint '/api/rest/1.0/ping' was called from '${request?.requestURI ?: "unknown"}'")
        val headers = HttpHeaders().apply { set("Warning", "Deprecated"); contentType = MediaType.TEXT_PLAIN }
        return ResponseEntity.ok().headers(headers).body("")
    }

    @Operation(summary = "Classifies plain text", tags = ["functional", "v1"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully classified",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = (ArraySchema(schema = Schema(implementation = SemanticResult::class)))
                )]
            ),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    @PostMapping(
        value = ["/api/rest/1.0/classifyText"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun classify(@RequestBody text: String?, request: HttpServletRequest?): ResponseEntity<String> {
        log.error("Endpoint '/api/rest/1.0/classifyText' was called from '${request?.requestURI ?: "unknown"}'")
        try {
            val result = classificator.classify(text)
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(gson.toJson(result))
        } catch (e: Exception) {
            log.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(null)
    }

    @Operation(summary = "Classifies the bytes of a document", tags = ["functional", "v1"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully classified",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = (ArraySchema(schema = Schema(implementation = SemanticResult::class)))
                )]
            ),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    @PostMapping(
        value = ["/api/rest/1.0/classify"],
        produces = [MediaType.TEXT_PLAIN_VALUE],
        consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    fun classify(@RequestBody content: ByteArray?, request: HttpServletRequest?): ResponseEntity<String> {
        log.error("Endpoint '/api/rest/1.0/classifyText' was called from '${request?.requestURI ?: "unknown"}'")
        try {
            val result = classificator.classify(content)
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(gson.toJson(result))
        } catch (e: Exception) {
            log.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(null)
    }

    companion object {
        private val gson = GsonBuilder().serializeNulls().create()
        private val log: Log = LogFactory.getLog(ClassificatorController::class.java)
    }
}