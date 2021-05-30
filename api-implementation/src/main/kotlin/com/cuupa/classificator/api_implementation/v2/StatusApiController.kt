package com.cuupa.classificator.api_implementation.v2

import com.cuupa.classificator.api.v2.StatusApi
import com.cuupa.classificator.engine.services.kb.KnowledgeBase
import com.google.gson.GsonBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.annotation.Generated

/**
 * @author Simon Thiel (https://github.com/cuupa) (29.05.2021)
 */
@Generated(value = ["io.swagger.codegen.v3.generators.java.SpringCodegen"], date = "2021-05-29T11:42:02.031Z[GMT]")
@RestController
open class StatusApiController @Autowired constructor(
    private val knowledgeBase: KnowledgeBase,
) : StatusApi {

    override fun statusGet(): ResponseEntity<String> {
        return try {
            val response = gson.toJson(
                com.cuupa.classificator.api_client.model.Status()
                    .apply {
                        status = "running"
                        knowledgebaseVersion = knowledgeBase.knowledgeBaseMetadata.version
                    })
            ResponseEntity(response, HttpStatus.OK)
        } catch (e: IOException) {
            log.error("Couldn't serialize response for content type application/json", e)
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(StatusApiController::class.java)
        private val gson = GsonBuilder().serializeNulls().create()
    }
}