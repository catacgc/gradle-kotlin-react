package io.catacgc.task

import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskProvider
import javax.inject.Inject

open class Karma @Inject constructor(private val config: TaskProvider<WebpackConfiguration>) : Exec() {

    init {
        group = "webpack"
        description = "run karma with configured via webpack"

        workingDir = project.projectDir
        dependsOn(config, "build")
    }

    override fun exec() {
        commandLine = """node_modules/.bin/karma start build/webpack-config/karma.conf.js"""
                .split("\n")
                .flatMap { it.trim().split(" ") }

        logger.lifecycle(commandLine.joinToString(" "))
        super.exec()
    }
}