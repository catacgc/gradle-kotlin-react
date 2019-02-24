package io.catacgc

import com.moowork.gradle.node.yarn.YarnTask
import io.catacgc.task.WebpackBuild
import io.catacgc.task.WebpackConfiguration
import io.catacgc.task.WebpackDevServer
import io.catacgc.task.WebpackTestMocha
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.StandardOutputListener
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

            tasks.withType(KotlinJsDce::class).configureEach {
                dceOptions.devMode = !isProdBuild

                logger.lifecycle("KotlinJsDce devMode is ${dceOptions.devMode}")
            }

            tasks.withType(Kotlin2JsCompile::class).configureEach {
                kotlinOptions.moduleKind = "commonjs"
                kotlinOptions.sourceMap = true
                kotlinOptions.sourceMapEmbedSources = "always"
            }
        }

        tasks {
            val webpackConfiguration = register<WebpackConfiguration>("webpackConfiguration")

            register<WebpackDevServer>("webpackDevServer", webpackConfiguration)
            register<WebpackBuild>("webpackBuild", webpackConfiguration)
            register<WebpackTestMocha>("webpackTestServer", webpackConfiguration)

            val init = register<YarnTask>("yarnInit") {
                args = listOf("init", "-y")
                outputs.files("package.json")
            }

            val devInstall = register<YarnTask>("yarnInstallDev") {
                group = "yarn"

                val dependencies = listOf(
                        "file-loader",
                        "css-loader",
                        "error-overlay-webpack-plugin",
                        "source-map-loader",
                        "style-loader",
                        "webpack",
                        "webpack-cli",
                        "webpack-dev-server",
                        "mocha",
                        "mocha-loader",
                        "null-loader",
                        "html-webpack-plugin"
                )

                args = listOf("add", "--dev", "--cwd", projectDir) + dependencies

                inputs.file("package.json")
                outputs.file("yarn.lock")
            }


            val mainInstall = register<YarnTask>("yarnInstallMain") {
                group = "yarn"

                val dependencies = listOf(
                        "core-js",
                        "react",
                        "react-dom"
                )

                args = listOf("add", "--cwd", projectDir) + dependencies

                inputs.file("package.json")
                outputs.file("yarn.lock")
            }

            register<YarnTask>("yarnInstall") {
                group = "yarn"

                dependsOn(init, devInstall, mainInstall)
            }
        }

    }

    /**
     * A hack to place the KotlinJsCompile error info in the webpack dev server view
     */
    private fun Project.captureKotlinJsErrorOutput() {
        val errorLog = file("$buildDir/error.log")
        val mainJsFile = file("$buildDir/kotlin-js-min/main/${project.name}.js")

        gradle.taskGraph.beforeTask {
            if (this is KotlinJsCompile) {
                logging.addStandardErrorListener { output ->
                    val message = output ?: return@addStandardErrorListener

                    val errorModule = """
                                (function() { throw new Error("$message"); } ())
                            """.trimIndent()

                    if (message.startsWith("e:")) {
                        println("writing stuff down")
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
