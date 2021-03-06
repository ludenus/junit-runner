package qa.runner

import org.apache.logging.log4j.kotlin.logger
import org.junit.platform.console.options.CommandLineOptions
import org.junit.platform.console.options.Details
import org.junit.platform.console.options.Theme
import org.junit.platform.console.tasks.ConsoleTestExecutor
import qa.runner.asm.ClassAnalyzer
import qa.runner.config.AppConfig
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.io.path.createTempDirectory


class JUnit5Runner(private val appConfig: AppConfig) : Callable<Int> {

    private val log = logger()
    private val entrypointClass = MainApplication::class.java
    private val mainClasspath = resolveMainClasspath()

    private val selectedPackages by lazy { selectedPackages() }

    override fun call(): Int {
        log.info("~~~~~~~ JUnit5Runner appConfig: $appConfig")
        val options = options()
        val writer = PrintWriter(System.out)
        val results = ConsoleTestExecutor(options).execute(writer)

        if (
            results.testsFoundCount == 0L ||
            results.totalFailureCount > 0 ||
            results.containersAbortedCount > 0
        ) {
            return 1
        }

        return 0
    }


    private fun selectedClasses(): List<String> {

        val cps = additionalClasspathEntries()

        val classes: List<String> = cps.flatMap { dir ->
            dir.toFile().walk()
                .filter {
                    it.isClassFile()
                }.filter {
                    ClassAnalyzer(it).hasJunitTestMethod
                }.map {
                    dir.relativize(it.toPath())
                }.filter {
                    it.belongsToSelectedPackage()
                }.map {
                    it.toClassName()
                }
        }.sortedBy {
            it
        }
        return classes

    }

    /**
     * using classloader.loadClass()  leads to failure when loading springboot ApplicationContext
     */
//    private fun selectedClasses_old(): List<String> {
//        val cps = additionalClasspathEntries()
//        val urls = cps.map { it.toUri().toURL() }.toTypedArray()
//        val classloader = URLClassLoader(urls)
//
//        val classes: List<String> = cps.flatMap { dir ->
//            dir.toFile().walk()
//                .filter {
//                    it.isClassFile()
//                }.map {
//                    dir.relativize(it.toPath())
//                }
//        }.map {
//            classloader.loadClass(it.toClassName())
//        }.filter {
//            it.hasJunitTestMethod() && it.belongsToSelectedPackage()
//        }.sortedBy {
//            it.name
//        }.map {
//            it.name
//        }
//
//        return classes
//    }


    private fun <T> List<T>.onlyForCurrentRunnerInstance() = this.filterIndexed { index, _ ->
        1 + (index % appConfig.instanceTotal) == appConfig.instanceNo
    }

    private fun Path.belongsToSelectedPackage() = selectedPackages.any { this.toClassName().startsWith(it) }

    private fun <T> Class<T>.belongsToSelectedPackage() = selectedPackages.any { this.packageName.startsWith(it) }

    private fun File.isClassFile(): Boolean = this.isFile && this.extension == "class"

    private fun Path.toClassName(): String = this.toString()
        .replace(File.separator, ".")
        .replace(".class", "")

    private fun Class<*>.hasJunitTestMethod(): Boolean = this.methods
        .map { it.annotations }
        .filter { it.isNotEmpty() }
        .flatMap { it.map { a -> a.annotationClass } }
        .distinct()
        .any { it.qualifiedName == "org.junit.jupiter.api.Test" }


    private fun options() = CommandLineOptions().apply {

        selectedClasses = selectedClasses().onlyForCurrentRunnerInstance()

//        selectedPackages = selectedPackages()
        selectedClasspathResources = selectedClasspathResources()
        additionalClasspathEntries = additionalClasspathEntries()

        isBannerDisabled = false
        theme = Theme.UNICODE
        details = Details.TREE
        isFailIfNoTests = true

        setReportsDir(reportsDir())
    }.also {
        log.info { "selectedPackages: ${it.selectedPackages}" }
        log.info { "selectedClasses: ${it.selectedClasses}" }
        log.info { "additionalClasspathEntries: ${it.additionalClasspathEntries}" }
        log.info { "selectedClasspathResources: ${it.selectedClasspathResources}" }
        log.info { "reportsDir : ${it.reportsDir.get()}" }
    }


    private fun reportsDir(): Path = createTempDirectory("reportDir")

    private fun selectedPackages(): List<String> {
        return (appConfig.selectedPackages ?: entrypointClass.packageName).split(",", ";", " ")
    }

    /*
        Directory tree is different for two supported types of invocation:
        1. local run (from IDE)
        2. docker container run
     */
    private fun selectedClasspathResources(): List<String> = setOf(
        mainClasspath.mainResourcesPath(),
        mainClasspath.testResourcesPath(),

        resolveResourcePath("main.resource").replaceAfterLast("/", "").dropLast(1),
//        resolveResourcePath("test.resource") // test resources are not copied by jib
    ).toList()

    private fun additionalClasspathEntries(): List<Path> = setOf(
        Path.of(mainClasspath.rootPath()),   // container lookup
        Path.of(mainClasspath.mainPath()),   // local lookup
        Path.of(mainClasspath.testPath()),   // local lookup
    ).toList()

    private fun String.rootPath(): String = this.replace("${entrypointClass.packageName}.*".toRegex(), "/")
    private fun String.mainPath(): String = this.replace("/main.*".toRegex(), "/main")
    private fun String.testPath(): String = this.replace("/main.*".toRegex(), "/test")

    private fun String.mainResourcesPath(): String = this.replace("/classes/kotlin/main.*".toRegex(), "/resources/main")
    private fun String.testResourcesPath(): String = this.replace("/classes/kotlin/main.*".toRegex(), "/resources/test")

    private fun resolveMainClasspath(): String {

        val className = entrypointClass.simpleName
        val classPackageDir = entrypointClass.packageName.replace(".", "/")
        val classResourceName = "$classPackageDir/$className.class"
        return resolveResourcePath(classResourceName).replaceAfterLast("/", "").dropLast(1)
    }

    private fun resolveResourcePath(resourceName: String): String {
        log.info("search: $resourceName")
        val resourceFile = entrypointClass.classLoader.getResource(resourceName)
            ?: throw FileNotFoundException(resourceName)
        log.info("found : $resourceFile")
        return resourceFile.path
    }

//    override fun run(vararg args: String?) {
//        execute()
//    }
//
//    override fun getExitCode(): Int {
//        TODO("Not yet implemented")
//    }
}