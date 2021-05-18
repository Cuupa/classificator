package com.cuupa.classificator.configuration.external

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("knowledge_base", "monitor")
class ClassificatorConfig {

    @JsonProperty("knowledge_base")
    var knowledgeBase: String? = null

    @JsonProperty("monitor")
    var monitorConfig: MonitorConfig? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
}