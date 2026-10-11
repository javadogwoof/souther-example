import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    kotlin("jvm") version "2.4.21"
    kotlin("plugin.spring") version "2.4.21"
    id("org.springframework.boot") version "4.0.7"
    id("org.souther-lang.souther") version "0.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(kotlin("reflect"))
    compileOnly(files(layout.buildDirectory.file("souther-model/souther-model.jar")))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(25)
}

souther {
    southerVersion = "0.2.0"
}

val southerModelJar by tasks.registering(Jar::class) {
    from(tasks.named("compileSouther"))
    archiveFileName = "souther-model.jar"
    destinationDirectory = layout.buildDirectory.dir("souther-model")
}

tasks.compileKotlin { dependsOn(southerModelJar) }

tasks.named<souther.gradle.SoutherCompile>("compileSouther") {
    compileClasspath.setFrom(configurations.compileClasspath.map { cp -> cp.filter { it.name != "souther-model.jar" } })
}

tasks.test {
    useJUnitPlatform()
}

dependencyLocking {
    lockAllConfigurations()
}
