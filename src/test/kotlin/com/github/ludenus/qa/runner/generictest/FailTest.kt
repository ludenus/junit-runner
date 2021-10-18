package com.github.ludenus.qa.runner.generictest

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

@Disabled
class FailTest {

    @Test
    fun checkFail() {

        fail("this test is expected to fail")

    }

}

