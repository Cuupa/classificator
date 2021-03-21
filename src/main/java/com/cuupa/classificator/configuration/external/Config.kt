package com.cuupa.classificator.configuration.external

import com.fasterxml.jackson.annotation.*


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("classificator")
class Config {

    @JsonProperty("classificator")
    var classificator: ClassificatorConfig? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
}