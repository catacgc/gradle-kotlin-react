
plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "0.10.1"
}

repositories {
    jcenter()
    maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
    implementation("com.moowork.gradle:gradle-node-plugin:1.2.0")
    compileOnly("org.jetbrains:annotations:16.0.2")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        create("reactPlugin") {
            id = "io.catacgc.kotlin-react"
            implementationClass = "io.catacgc.KotlinReactPluginEntry"
            version = fetchVersionFromReadme()
        }
    }
}

pluginBundle {
    website = "https://github.com/catacgc/gradle-kotlin-react"
    vcsUrl = "https://github.com/catacgc/gradle-kotlin-react"
    description = "Simplified Kotlin React Workflow"

    (plugins) {
        "reactPlugin" {
            // id is captured from java-gradle-plugin configuration
            displayName = "Gradle Kotlin React Plugin"
            tags = listOf("kotlin", "react", "javascript", "webpack")
            version = fetchVersionFromReadme()
        }
    }
}

fun fetchVersionFromReadme(): String {
    return File("README.md")
            .readLines()
            .first { it.contains("""id("io.catacgc.kotlin-react")""") }
            .split('"')
            .takeLast(2)
            .first()
}
