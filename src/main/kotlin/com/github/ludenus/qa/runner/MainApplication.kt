package com.github.ludenus.qa.runner

import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.system.exitProcess


@SpringBootApplication
class MainApplication {

    companion object Main {
        @JvmStatic
        fun main(args: Array<String>) {
//            exitProcess(CommandLine(CommandParse()).execute(*args))
            exitProcess(JUnit5Runner().call())
        }
    }

}