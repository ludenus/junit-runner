package qa.runner.command

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.stereotype.Component
import qa.runner.config.AppConfig

@Component
class MainApplicationRunner(val mainCommand: MainCommand, val appConfig: AppConfig) : CommandLineRunner, ExitCodeGenerator {

    // https://github.com/remkop/picocli/tree/master/picocli-spring-boot-starter

    private var exitCode = 0

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        exitCode = MainCommand(appConfig).call()
    }

    override fun getExitCode(): Int = exitCode

}