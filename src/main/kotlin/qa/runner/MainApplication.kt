package qa.runner

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import qa.runner.config.AppConfig
import kotlin.system.exitProcess


@SpringBootApplication
@EnableConfigurationProperties(AppConfig::class)
class MainApplication {

    companion object Main {

        @JvmStatic
        fun main(args: Array<String>) {
            exitProcess(SpringApplication.exit(
                runApplication<MainApplication>(*args) {
                    setBannerMode(Banner.Mode.CONSOLE)
                }
            ))
        }
    }

}

