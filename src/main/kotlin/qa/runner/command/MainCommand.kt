package qa.runner.command

import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Component
import picocli.CommandLine
import qa.runner.JUnit5Runner
import qa.runner.RecursiveRun
import qa.runner.Version
import qa.runner.config.AppConfig
import java.io.File
import java.util.concurrent.Callable


@Component
@CommandLine.Command(mixinStandardHelpOptions = true)
class MainCommand(private val appConfig: AppConfig) : Callable<Int> {

    private val log = logger()

    @CommandLine.Option(
        names = ["-i", "--in-file"],
        required = false,
        paramLabel = "FILE_TO_READ",
        description = ["input file"]
    )
    var fileIn: File? = null

    @CommandLine.Option(
        names = ["-o", "--out-file"],
        required = false,
        paramLabel = "FILE_TO_WRITE",
        description = ["output file"]
    )
    var fileOut: File? = null

    @CommandLine.Option(
        names = ["-t", "--token"],
        required = false,
        paramLabel = "TOKEN",
        description = ["token"]
    )
    var token: String? = null


    override fun call(): Int = if (!RecursiveRun.isPerformed) {
        logGitProperties()
        JUnit5Runner(appConfig).call()
    } else {
        log.info { "prevent recursive run" }
        0
    }

    fun logGitProperties() {
        Version.gitProperties.forEach { k, v ->
            log.info { "$k: $v" }
        }
    }
}
