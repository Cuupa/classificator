package com.cuupa.classificator.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
@Validated
@Deprecated(message = "Deprecated for API version 2")
interface PingApi {

    @Operation(summary = "Retrieves the application status", description = "", tags = ["health-check", "v1"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "The status of the application",
            content = [Content(schema = Schema(implementation = String::class))]
        )]
    )
    @RequestMapping(value = ["/api/rest/1.0/ping"], produces = ["text/plain"], method = [RequestMethod.GET])
    fun ping(request: HttpServletRequest?): ResponseEntity<String>
}