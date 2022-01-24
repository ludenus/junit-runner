package qa.runner

import org.apache.logging.log4j.kotlin.logger
import java.util.*

object Version {

    private val log = logger()

    val gitProperties: Properties by lazy {
        Properties().apply {
            try {
                val res = Version::class.java.classLoader.getResourceAsStream("git.properties")
                this.load(res)
            } catch (e: Exception) {
                log.error { "failed to load git.properties" }
            }
        }
    }

}