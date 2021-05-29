package com.cuupa.classificator.api.v2.model

/**
 * @author Simon Thiel (https://github.com/cuupa) (29.05.2021)
 */
abstract class ApiModel {

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    protected fun toIndentedString(o: Any?): String {
        return o?.toString()?.replace("\n", "\n    ") ?: "null"
    }
}
