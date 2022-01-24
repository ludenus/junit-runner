package qa.runner.generictest

import io.qameta.allure.Feature
import io.qameta.allure.Step
import org.apache.logging.log4j.kotlin.logger
import org.junit.jupiter.api.Test

@Feature("generic test")
class LogTest {

    private val log = logger()

    @Test
    fun checkLogLevels() {
        logAllLogLevels()
    }

    @Step
    fun logAllLogLevels() {
        log.error { "error something" }
        log.warn { "warn something" }
        log.info { "info something" }
        log.debug { "debug something" }
        log.trace { "trace something" }
    }
}

