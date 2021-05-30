package com.cuupa.classificator.api_implementation.v2

import com.cuupa.classificator.api.v2.api.ClassificationApi
import com.cuupa.classificator.api_client.model.ClassificationMetadata
import com.cuupa.classificator.api_client.model.ClassificationRequest
import com.cuupa.classificator.api_client.model.ClassificationResult
import com.cuupa.classificator.api_client.model.SemanticResult
import com.cuupa.classificator.api_implementation.CustomOffsetDateTimeSerializer
import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.services.kb.KnowledgeBase
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.annotation.Generated
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


/**
 * @author Simon Thiel (https://github.com/cuupa) (29.05.2021)
 */
@Generated(value = ["io.swagger.codegen.v3.generators.java.SpringCodegen"], date = "2021-05-29T11:42:02.031Z[GMT]")
@RestController
open class ClassificationApiController @Autowired constructor(
    private val classificator: Classificator,
    private val knowledgeBase: KnowledgeBase,
    private val request: HttpServletRequest
) : ClassificationApi {

    override fun classifyPost(
        @Parameter(
            `in` = ParameterIn.DEFAULT,
            description = "The classification request",
            required = true,
            schema = Schema()
        ) @RequestBody body: @Valid ClassificationRequest?
    ): ResponseEntity<String> {
        val accept = request.getHeader("Accept") ?: ""
        return if (accept.contains("application/json")) {
            try {
                val start = LocalDateTime.now()
                val (contentType, classificationResult) = classificator.classify(body?.contentType, body?.content)
                val stop = LocalDateTime.now()
                val result = getResult(classificationResult, start, stop, contentType)
                ResponseEntity(gson.toJson(result), HttpStatus.OK)
            } catch (e: IOException) {
                log.error("Couldn't serialize response for content type application/json", e)
                val error = com.cuupa.classificator.api_client.model.Error().apply {
                    code = "500"
                    message = "Internal Server Error"
                }
                ResponseEntity(gson.toJson(error), HttpStatus.INTERNAL_SERVER_ERROR)
            }
        } else {
            val error = com.cuupa.classificator.api_client.model.Error().apply {
                code = "501"
                message = "Not Implemented for content type $accept"
            }
            ResponseEntity(gson.toJson(error), HttpStatus.NOT_IMPLEMENTED)
        }
    }

    private fun getResult(
        classificationResults: List<com.cuupa.classificator.domain.SemanticResult>,
        start: LocalDateTime,
        stop: LocalDateTime,
        contentType: String
    ) = ClassificationResult().apply {
        results = createResults(classificationResults)
        info = createResultInfo(start, stop)
        this.contentType = contentType
    }

    private fun createResultInfo(start: LocalDateTime, stop: LocalDateTime) = ClassificationMetadata().apply {
        processedAt = start.atOffset(ZoneId.systemDefault().rules.getOffset(start))
        processingTime = "${Duration.between(start, stop).toMillis()} ms"
        knowledgebaseVersion = knowledgeBase.knowledgeBaseMetadata.version
    }

    private fun createResults(classificationResult: List<com.cuupa.classificator.domain.SemanticResult>) =
        classificationResult.map { result ->
            SemanticResult().apply {
                topic = result.topic
                sender = result.sender
                metadata = result.metadata.map { metadata ->
                    com.cuupa.classificator.api_client.model.Metadata().apply {
                        name = metadata.name
                        value = metadata.value
                    }
                }
            }

        }.toMutableList()

    companion object {
        private val log = LoggerFactory.getLogger(ClassificationApiController::class.java)
        private val gson = GsonBuilder().serializeNulls()
            .registerTypeAdapter(OffsetDateTime::class.java, CustomOffsetDateTimeSerializer()).create()
    }
}