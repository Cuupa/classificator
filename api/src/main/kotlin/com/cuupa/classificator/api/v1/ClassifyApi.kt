package com.cuupa.classificator.api.v1

import com.cuupa.classificator.domain.SemanticResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.servlet.http.HttpServletRequest

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@Validated
@Deprecated(message = "Deprecated for API version 2")
interface ClassifyApi {

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
    fun classify(@RequestBody text: String?, request: HttpServletRequest?): ResponseEntity<String>

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
    fun classify(@RequestBody content: ByteArray?, request: HttpServletRequest?): ResponseEntity<String>
}