package com.cuupa.classificator.api.v2

import javax.annotation.Generated

/**
 * @author Simon Thiel (https://github.com/cuupa) (29.05.2021)
 */
@Generated(value = ["io.swagger.codegen.v3.generators.java.SpringCodegen"], date = "2021-05-29T11:42:02.031Z[GMT]")
class NotFoundException(private val code: Int, msg: String?) : ApiException(
    code, msg
)