package com.cuupa.classificator.api.v2.api

import javax.annotation.Generated
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlTransient

/**
 * @author Simon Thiel (https://github.com/cuupa) (29.05.2021)
 */
@Generated(value = ["io.swagger.codegen.v3.generators.java.SpringCodegen"], date = "2021-05-29T11:42:02.031Z[GMT]")
@XmlRootElement
class ApiResponseMessage {
    @get:XmlTransient
    var code = 0
    var type: String? = null
    var message: String? = null

    constructor() {}
    constructor(code: Int, message: String?) {
        this.code = code
        type = when (code) {
            ERROR -> "error"
            WARNING -> "warning"
            INFO -> "info"
            OK -> "ok"
            TOO_BUSY -> "too busy"
            else -> "unknown"
        }
        this.message = message
    }

    companion object {
        const val ERROR = 1
        const val WARNING = 2
        const val INFO = 3
        const val OK = 4
        const val TOO_BUSY = 5
    }
}