package qa.runner.extensions

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

val objectMapper by lazy {
    ObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        enable(SerializationFeature.INDENT_OUTPUT, SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
    }
}

fun Any.toJson() = objectMapper.writeValueAsString(this)

fun Any.toJsonBytes() = objectMapper.writeValueAsBytes(this)