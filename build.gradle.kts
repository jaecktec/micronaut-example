import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val micronautVersion: String by project
val micronautDataHibernateJpaVersion = "1.0.0.M5"


plugins {
    application
    idea
    kotlin("jvm") version "1.3.60"
    kotlin("kapt") version "1.3.60"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.60"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.60"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

tasks {
    withType<ShadowJar> {
        manifest.attributes.apply {
            put("Application-Name", project.name)
            put("Build-Date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME))
            put("Created-By", System.getProperty("user.name"))
            put("Gradle-Version", gradle.gradleVersion)
            put("Implementation-Version", "${project.version}")
            put("JDK-Version", System.getProperty("java.version"))
        }
        mergeServiceFiles()
    }
}

repositories {
    jcenter()
}

noArg {
    annotation("javax.persistence.Entity")
}

allOpen {
	annotations(
            "io.micronaut.aop.Around",
            "io.micronaut.http.annotation.Controller",
            "javax.inject.Singleton"
    )
}
val run by tasks.getting(JavaExec::class)
run.jvmArgs("-noverify", "-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    testImplementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))

    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("javax.annotation:javax.annotation-api")

    kapt("io.micronaut.data:micronaut-data-processor:$micronautDataHibernateJpaVersion")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa:$micronautDataHibernateJpaVersion")
//    runtime("io.micronaut.configuration:micronaut-jdbc-tomcat")
    runtime("io.micronaut.configuration:micronaut-jdbc-hikari")
    runtime("com.h2database:h2")

    kapt("io.micronaut:micronaut-inject-java")
    kaptTest("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")

    runtime("ch.qos.logback:logback-classic:1.2.3")
    runtime("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")

    implementation("io.micronaut:micronaut-validation")

    testAnnotationProcessor("io.micronaut:micronaut-inject-java")

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    // Define the main class for the application
    mainClassName = "com.micronaut.example.App"
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}