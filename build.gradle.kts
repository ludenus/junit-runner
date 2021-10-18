import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig
import pl.allegro.tech.build.axion.release.domain.properties.TagProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("kapt") version "1.5.30"

    id("com.gorylenko.gradle-git-properties") version "2.2.4"
    id("pl.allegro.tech.build.axion-release") version "1.11.0"

    id("com.google.cloud.tools.jib") version "3.1.4"
}

scmVersion {
    // https://axion-release-plugin.readthedocs.io/en/latest

    versionIncrementer("incrementPatch")

    versionCreator(KotlinClosure2({ v: String, s: ScmPosition ->

        "${v}-${s.branch}-${s.shortRevision}"
    }))

    tag(closureOf<TagNameSerializationConfig> {
        initialVersion = KotlinClosure2<TagProperties, ScmPosition, String>({ _, _ -> "0.0.1" }) // use if no tags found
        prefix =
            ""           // when calculating version from git tags, only tags that start from prefix are taken into account
        versionSeparator = "" // no separator expected between prefix and version number e.g. v0.0.3
    })
}

group = "com.github.ludenus"
version = "${scmVersion?.version}"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation("org.apache.logging.log4j:log4j-core:2.14.1")


//    implementation("info.picocli:picocli:4.6.1")
    kapt ("info.picocli:picocli-codegen:4.6.1")
    implementation("info.picocli:picocli-spring-boot-starter:4.6.1")
    annotationProcessor("info.picocli:picocli-codegen:4.6.1")

    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.junit.jupiter:junit-jupiter:5.7.2")

    implementation("org.junit.platform:junit-platform-console:1.8.1")
    implementation("org.junit.platform:junit-platform-launcher:1.8.1")

//    kapt("org.springframework.boot:spring-boot-configuration-processor")
}


kapt {
    correctErrorTypes = true

    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}

jib {

    from {
        image = "adoptopenjdk/openjdk11:jdk-11.0.12_7-alpine"
    }
    to {
        image = "ludenus/${name}"
        tags = setOf("${scmVersion.version}", "latest", "${scmVersion.scmPosition.branch}")
    }
    container {
        // https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#container-closure
        appRoot = "/app"
        workingDirectory = "/"
        entrypoint = listOf("/entrypoint.sh")
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
    extraDirectories {
        paths {
            path {
                setFrom("src/main/jib")
                into = "${container.workingDirectory}"
            }
            path {
                setFrom("build/classes/kotlin/test")
                into = "${container.appRoot}/classes"
            }
        }
        permissions = mapOf("/entrypoint.sh" to "755")
    }

}

gitProperties {
    failOnNoGitDirectory = false
    keys = mutableListOf(
        "git.commit.id",
        "git.commit.time",
        "git.branch",
        "git.commit.message.full",
        "git.commit.user.name",
        "git.commit.id.abbrev"
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict") // treat nullability warnings as errors
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}


configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
}
