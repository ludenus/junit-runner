package qa.runner.distributed

import io.qameta.allure.Feature
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@Feature("distributed test")
class Test02 {

    @Test
    fun check() {
        Assertions.assertTrue(true)
    }

}

