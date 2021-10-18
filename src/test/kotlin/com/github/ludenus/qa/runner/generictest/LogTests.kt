package com.github.ludenus.qa.runner.generictest

import org.apache.logging.log4j.kotlin.logger
import org.junit.jupiter.api.Test

class LogTests {

    private val log = logger()

    @Test
    fun logSomething() {
        log.warn { "warn something" }
        log.info { "info something" }
        log.debug { "debug something" }
        log.trace { "trace something" }
    }

}

