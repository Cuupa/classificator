package com.cuupa.classificator.controller

import com.cuupa.classificator.services.Classificator
import com.google.gson.Gson
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassificatorController(private val classificator: Classificator) {

    private val LOG: Log = LogFactory.getLog(ClassificatorController::class.java)

    @RequestMapping("/ping")
    fun ping(): ResponseEntity<String> {
        return ResponseEntity.ok().body("200")
    }

    @RequestMapping(value = ["/classifyText"], method = [RequestMethod.POST])
    fun classify(@RequestBody text: String?): ResponseEntity<String?> {
        try {
            val result = classificator.classify(text)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(gson.toJson(result))
        } catch (e: Exception) {
            LOG.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
    }

    @RequestMapping(value = ["/classify"], method = [RequestMethod.POST])
    fun classify(@RequestBody content: ByteArray?): ResponseEntity<String?> {
        try {
            val result = classificator.classify(content)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(gson.toJson(result))
        } catch (e: Exception) {
            LOG.error(e)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
    }

    companion object {
        private val gson = Gson()
    }

}