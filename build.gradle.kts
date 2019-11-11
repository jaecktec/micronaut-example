import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val micronautVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.3.50"
    kotlin("kapt") version "1.3.50"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.50"

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

allOpen {
	annotations(
            "io.micronaut.aop.Around",
            "io.micronaut.http.annotation.Controller"
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
    // group 1
    compile(kotlin("stdlib-jdk8"))  
    compile(kotlin("reflect"))  
    // end group 1

    // group 2
    implementation("io.micronaut:micronaut-management:$micronautVersion")  
    implementation("io.micronaut:micronaut-runtime:$micronautVersion")  
    implementation("io.micronaut:micronaut-http-client:$micronautVersion")  
    implementation("io.micronaut:micronaut-http-server-netty:$micronautVersion")  
    implementation("javax.annotation:javax.annotation-api")  
  
    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))  
    kapt("io.micronaut:micronaut-inject-java:$micronautVersion")  
    kapt("io.micronaut:micronaut-validation:$micronautVersion")  
    // end group 2  


    // group 3
    runtime("ch.qos.logback:logback-classic:1.2.3")  
    runtime("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")  
    // end group 3
    
    // group 4
    implementation("io.micronaut:micronaut-validation:$micronautVersion")  
    // end group 4
  
    // group 5
    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))  
    kaptTest("io.micronaut:micronaut-inject-java:$micronautVersion")  
    testAnnotationProcessor("io.micronaut:micronaut-inject-java:$micronautVersion")  
    testImplementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))  
    // end group 5


    // group 6
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api")  
    testImplementation("io.micronaut.test:micronaut-test-junit5")  
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")  
    // end group 6
}

application {
    // Define the main class for the application
    mainClassName = "com.micronaut.example.App"
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}