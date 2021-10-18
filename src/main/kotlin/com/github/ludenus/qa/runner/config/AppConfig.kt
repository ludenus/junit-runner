package com.github.ludenus.qa.runner.config

import com.github.ludenus.qa.runner.validation.UID
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty


@Validated
@Component
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class AppConfig (

    @field:NotEmpty
    var label: String = "",

    @field:UID
    var id: String = "",
)
