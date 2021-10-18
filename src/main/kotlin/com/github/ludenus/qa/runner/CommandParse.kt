package com.github.ludenus.qa.runner

import org.apache.logging.log4j.kotlin.logger
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable

class CommandParse : Callable<Int> {

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

//    @CommandLine.Parameters(paramLabel = "FILE", description = ["one ore more files"])
//    lateinit var files: Array<File>

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display help message"])
    var helpRequested: Boolean = false;

    override fun call(): Int {
        println("~~~~~~~~~~~~~~~~~~~~~ check LOG_LEVEL: ${System.getenv("LOG_LEVEL")}")

        log.warn { "check warn" }
        log.info { "check info" }
        log.debug { "check debug" }

//        JUnit5Runner1.main(emptyArray())
        JUnit5Runner().call()
        println("~~~~~~~~~~~~~~~~~~~~~ done")

        // TBD
        return 0
    }
}
