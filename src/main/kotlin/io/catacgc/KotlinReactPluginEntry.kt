package io.catacgc

import com.moowork.gradle.node.yarn.YarnTask
import io.catacgc.task.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJsDce
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

class KotlinReactPluginEntry : Plugin<Project> {

    override fun apply(project: Project) {
        project.logger.lifecycle("Project name is: ${project.name}")

        with(project) {
            applyPlugins()
            addJsRepositories()
            configureJsTarget()
            captureKotlinJsErrorOutput()
        }
    }

    private fun Project.addJsRepositories() {
        repositories {
            maven("https://kotlin.bintray.com/kotlin-js-wrappers")
            maven("https://kotlin.bintray.com/kotlinx")
            maven("https://dl.bintray.com/kotlin/kotlinx.html")
            mavenCentral()
            jcenter()
        }
    }

    private fun Project.applyPlugins() {
        plugins.apply("kotlin-dce-js")
        plugins.apply("com.moowork.node")
    }

    private fun Project.configureJsTarget() {

        dependencies {
            "implementation"(kotlin("stdlib-js"))
            "implementation"("org.jetbrains:kotlin-react-dom:16.6.0-pre.68-kotlin-1.3.20")
            "testImplementation"("org.jetbrains:kotlin-mocha:3.0.1-pre.68-kotlin-1.3.20")
            "testImplementation"("org.jetbrains.kotlin:kotlin-test-js")
        }
        gradle.taskGraph.whenReady {
            val isProdBuild = allTasks.any { it.name == "webpackBuild" }
            val isTestBuild = allTasks.any { it.name in setOf("webpackTestServer", "karma") }

            tasks.withType(KotlinJsDce::class).configureEach {
                dceOptions.devMode = !isProdBuild

                logger.lifecycle("KotlinJsDce devMode is ${dceOptions.devMode}")
            }

            tasks.withType(Kotlin2JsCompile::class).configureEach {
                if (isTestBuild) {
                    logger.lifecycle("Compiling for tests, not calling main")
                    kotlinOptions.main = "noCall"
                }

                kotlinOptions.moduleKind = "commonjs"
                kotlinOptions.sourceMap = true
            }
        }

        tasks {
            val webpackConfiguration = register<WebpackConfiguration>("webpackConfiguration")

            register<WebpackDevServer>("webpackDevServer", webpackConfiguration)
            register<WebpackBuild>("webpackBuild", webpackConfiguration)
            register<WebpackTestMocha>("webpackTestServer", webpackConfiguration)
            register<Karma>("karma", webpackConfiguration)

            val init = register<YarnTask>("yarnInit") {
                args = listOf("init", "-y")
                outputs.files("package.json")
            }

            val devInstall = register<YarnTask>("yarnInstallDev") {
                group = "yarn"

                val dependencies = listOf(
                        "file-loader@3.0.1",
                        "css-loader@2.1.0",
                        "error-overlay-webpack-plugin@0.1.6",
                        "source-map-loader@0.2.4",
                        "style-loader@0.23.1",
                        "webpack@4.29.5",
                        "webpack-cli@3.2.3",
                        "webpack-dev-server@3.2.0",
                        "mocha@6.0.1",
                        "mocha-loader@2.0.0",
                        "null-loader@0.1.1",
                        "html-webpack-plugin@3.2.0",
                        "karma@4.0.0",
                        "karma-mocha@1.3.0",
                        "karma-webpack@3.0.5",
                        "karma-chrome-launcher@2.2.0",
                        "karma-sourcemap-loader@0.3.7"
                )

                args = listOf("add", "--dev", "--cwd", projectDir) + dependencies

                inputs.file("package.json")
                outputs.file("yarn.lock")
            }


            val mainInstall = register<YarnTask>("yarnInstallMain") {
                group = "yarn"

                val dependencies = listOf(
                        "core-js@2.6.5",
                        "react@16.8.3",
                        "react-dom@16.8.3"
                )

                args = listOf("add", "--cwd", projectDir) + dependencies

                inputs.file("package.json")
                outputs.file("yarn.lock")
            }

            val yarnInstall = register<YarnTask>("yarnInstall") {
                group = "yarn"

                dependsOn(init, devInstall, mainInstall)

                inputs.file("package.json")
                outputs.file("yarn.lock")
            }

            named("build") {
                dependsOn(yarnInstall)
            }
        }

    }

    /**
     * A hack to place the KotlinJsCompile error info in the webpack dev server view
     */
    private fun Project.captureKotlinJsErrorOutput() {
        val errorLog = file("$buildDir/kotlin-compile-error.txt")
        val mainJsFile = file("$buildDir/kotlin-js-min/main/${project.name}.js")

        gradle.taskGraph.beforeTask {
            if (this is KotlinJsCompile) {
                logging.addStandardErrorListener { output ->
                    val message = output ?: return@addStandardErrorListener

                    val errorModule = """
                                (function() { throw new Error("$message"); } ())
                            """.trimIndent()

                    if (message.startsWith("e:")) {
                        logger.lifecycle("Capturing errors")
                        errorLog.createNewFile()
                        mainJsFile.writeText(errorModule)
                    }
                }
            }
        }

        gradle.taskGraph.afterTask {
            if (this is KotlinJsCompile) {
                if (state.failure == null) {
                    if (errorLog.exists()) {
                        errorLog.delete()
                        mainJsFile.delete()
                    }
                }
            }
        }
    }

}
