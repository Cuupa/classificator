package com.cuupa.classificator.externalconfiguration

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("knowledge_base", "monitor")
class ClassificatorConfig {

    @JsonProperty("knowledge_base")
    var knowledgeBase: String? = null

    @JsonProperty("api_key_required")
    var apiKeyRequired: Boolean? = false

    fun isApiKeyRequired() = apiKeyRequired ?: false

    @JsonProperty("admin")
    var adminConfig: AdminConfig? = null

    @JsonProperty("monitor")
    var monitorConfig: MonitorConfig? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
}