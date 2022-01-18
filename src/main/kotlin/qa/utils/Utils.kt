package qa.utils

import org.apache.logging.log4j.kotlin.KotlinLogger


fun <V> KotlinLogger.logMsec(label: String, block: () -> V): V {
    this.info("[$label] --------------------> begin")

    val start = System.currentTimeMillis()
    val res = block()
    val end = System.currentTimeMillis()
    val millis = end - start
    this.info { "[$label] done in ${millis} ms" }
    this.info { "[$label] <-------------------- end" }
    return res
}