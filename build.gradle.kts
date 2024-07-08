plugins {
    id("org.springframework.boot") version "3.2.7"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.flywaydb.flyway") version "10.15.2"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "br.com.crudcep"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val testAgent by configurations.creating

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate.validator:hibernate-validator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.0")

    implementation("org.flywaydb:flyway-core")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.github.tomakehurst:wiremock:1.58")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAgent("net.bytebuddy:byte-buddy-agent:1.14.6")
}



testing {

    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")
            dependencies {
                implementation("io.mockk:mockk:1.13.9")
            }
            targets.configureEach {
                testTask.configure {
                    jvmArgs(testAgent.files.map { "-javaagent:${it.absolutePath}" }) // comment this out to see warning
                }
            }
        }
    }
}

flyway {
    url = "jdbc:h2:mem:crud-cep"
    user = "sa"
    password = ""
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
