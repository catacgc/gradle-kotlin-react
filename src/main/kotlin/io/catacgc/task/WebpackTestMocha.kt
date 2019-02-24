package io.catacgc.task

import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskProvider
import javax.inject.Inject

open class WebpackTestMocha @Inject constructor(private val config: TaskProvider<WebpackConfiguration>) : Exec() {

    init {
        group = "webpack"
        description = "start a webpack test server; make sure you run ./gradle build before"

        workingDir = project.projectDir
        dependsOn(config, "yarnInstall", "build")
    }

    override fun exec() {
        commandLine = """node_modules/.bin/webpack-dev-server
                        --context ${project.projectDir}
                        --config ${config.get().testOutputFile.get()}
                        --require source-map-support/register
                        mocha-loader!./build/kotlin-js-min/test/${project.name}_test.js"""
                .split("\n")
                .flatMap { it.trim().split(" ") }

        logger.lifecycle(commandLine.joinToString(" "))
        super.exec()
    }
}