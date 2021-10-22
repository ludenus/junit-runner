package com.github.ludenus.qa.runner.generictest

import io.qameta.allure.Feature
import io.qameta.allure.Step
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

@Disabled
@Feature("disabled test")
class FailTest {

    @Test
    fun checkFail() {
        failInsideStep()
    }

    @Step
    fun failInsideStep(): Nothing = fail("this test is expected to fail")
}

