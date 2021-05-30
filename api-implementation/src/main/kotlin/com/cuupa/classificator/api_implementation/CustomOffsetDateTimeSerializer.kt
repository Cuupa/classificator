package com.cuupa.classificator.api_implementation

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class CustomOffsetDateTimeSerializer : JsonSerializer<OffsetDateTime> {
    override fun serialize(src: OffsetDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    companion object {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }
}
