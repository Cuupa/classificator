package com.cuupa.classificator.controller

import com.cuupa.classificator.knowledgebase.Classificator
import com.google.gson.GsonBuilder
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassificatorController(private val classificator: Classificator) {

    private val log: Log = LogFactory.getLog(ClassificatorController::class.java)

    @GetMapping(value = ["/api/rest/1.0/ping"])
    fun ping(): ResponseEntity<String> = ResponseEntity.ok().body("200")

    @PostMapping(value = ["/api/rest/1.0/classifyText"])
    fun classify(@RequestBody text: String?): ResponseEntity<String> {
        try {
            val result = classificator.classify(text)
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(result))
        } catch (e: Exception) {
            log.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
    }

    @PostMapping(value = ["/api/rest/1.0/classify"])
    fun classify(@RequestBody content: ByteArray?): ResponseEntity<String> {
        try {
            val result = classificator.classify(content)
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(result))
        } catch (e: Exception) {
            log.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
    }

    companion object {
        private val gson = GsonBuilder().serializeNulls().create()
    }
}