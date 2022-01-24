package qa.runner.springboottest

import io.qameta.allure.Feature
import org.apache.logging.log4j.kotlin.logger
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import qa.runner.config.AppConfig
import qa.runner.extensions.toJson

@SpringBootTest
@ActiveProfiles("test")
@Feature("springboot test")
class MainApplicationTest {

    @Autowired
    private lateinit var appConfig: AppConfig

    private val log = logger()

    @Test
    fun contextLoads() {
        log.info { "------- MainApplicationTests appConfig: ${appConfig.toJson()} " }
    }

}
