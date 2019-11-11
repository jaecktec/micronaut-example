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

// This part makes sure, that we have a self contained jar with all dependencies
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


// AllOpen adds open to all Classes annotated with 
// @Around and @Controller. 
// This is needed for the @Valid capability
allOpen {
	annotations(
            "io.micronaut.aop.Around",
            "io.micronaut.http.annotation.Controller"
    )
}

val run by tasks.getting(JavaExec::class)
run.jvmArgs("-noverify", "-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")

// this will set the JVM Target to 1.8 and enable strict null checking while compiling. 
// This saved me already from having strange effects while executing the code.
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}


dependencies {
    // this part is the 'bill of material' (bom). 
    // It manages which dependency versions we are going to use
    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))  
    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))  
    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))  
    testImplementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))  
    
    // required Micronaut depenencies for a webservice
    implementation("io.micronaut:micronaut-management:$micronautVersion")  
    implementation("io.micronaut:micronaut-runtime:$micronautVersion")  
    implementation("io.micronaut:micronaut-http-client:$micronautVersion")  
    implementation("io.micronaut:micronaut-http-server-netty:$micronautVersion")  
    implementation("javax.annotation:javax.annotation-api")  

    // well jdk stuff
    implementation(kotlin("stdlib-jdk8"))  
    implementation(kotlin("reflect"))  

    // Micronaut annotation preprocessor dependencies
    kapt("io.micronaut:micronaut-inject-java:$micronautVersion")  
    kapt("io.micronaut:micronaut-validation:$micronautVersion")  


    // runtime dependencies for unmarshalling json and logging  
    runtime("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")  
    runtime("ch.qos.logback:logback-classic:1.2.3")  
    // end group 3
    
    // optional dependency (for now) to make use of @Valid
    implementation("io.micronaut:micronaut-validation:$micronautVersion")  

    // Micronaut annotation preprocessor dependencies for test 
    kaptTest("io.micronaut:micronaut-inject-java:$micronautVersion")  

    // Junit, Micronaut-Test and Mockk
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api")  
    testImplementation("io.micronaut.test:micronaut-test-junit5")  
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")  
}


application {
    // Define the main class for the application
    mainClassName = "com.micronaut.example.App"
}

// tell gradle to use junit5 runner
val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}