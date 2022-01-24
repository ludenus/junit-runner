package qa.runner.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import qa.runner.validation.UID
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty


@Validated
@Component
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class AppConfig(

    @field:NotEmpty
    var label: String = "",

    @field:UID
    var id: String = "",

    var selectedPackages: String? = null,

    @field:Min(1)
    var instanceNo: Int = 1,

    @field:Min(1)
    var instanceTotal: Int = 1,
)
