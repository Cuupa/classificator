package com.cuupa.classificator.externalconfiguration

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("classificator")
open class Config {

    @JsonProperty("classificator")
    var classificator: ClassificatorConfig? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
}