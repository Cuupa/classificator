package com.cuupa.classificator.api

import com.cuupa.classificator.api.version1.response.ResponseVersion1
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

@RestController
@RequestMapping(value = ["/api/rest/1.0"])
class ClassificatorControllerVersion1(private val classificator: Classificator) {

    private val log: Log = LogFactory.getLog(ClassificatorControllerVersion1::class.java)

    private val headers = HttpHeaders().apply { set("script-src", "self"); contentType = MediaType.APPLICATION_JSON }

    @GetMapping(value = ["/status"])
    fun status(): ResponseEntity<String> = ResponseEntity.ok().headers(headers).body(gson.toJson(ResponseVersion1("/status")))

    @Operation(summary = "Classifies plain text")
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
        value = ["/classifyText"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun classify(@RequestBody text: String?): ResponseEntity<String> {
        try {
            val result = classificator.classify(text)
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(gson.toJson(result))
        } catch (e: Exception) {
            log.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(null)
    }

    @Operation(summary = "Classifies the bytes of a document")
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
        value = ["/classify"],
        produces = [MediaType.TEXT_PLAIN_VALUE],
        consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    fun classify(@RequestBody content: ByteArray?): ResponseEntity<String> {
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
    }
}