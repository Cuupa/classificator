package com.cuupa.classificator.configuration.external

import com.fasterxml.jackson.annotation.*


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("username", "password", "enabled", "logText", "database-name")
class MonitorConfig {

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("password")
    var password: String? = null

    @JsonProperty("enabled")
    var enabled: Boolean? = null

    @JsonProperty("logText")
    var logText: Boolean? = null

    @JsonProperty("database-name")
    var databaseName: String? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
}