package com.cuupa.classificator.externalconfiguration

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("username", "password")
class AdminConfig {

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("password")
    var password: String? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
}
