package io.catacgc.task

import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskProvider
import javax.inject.Inject

open class WebpackDevServer @Inject constructor(private val config: TaskProvider<WebpackConfiguration>) : Exec() {

    init {
        group = "webpack"
        description = "start a webpack dev server; make sure you run ./gradle build before"

        workingDir = project.projectDir
        dependsOn(config, "build")
    }

    override fun exec() {
        commandLine = """node_modules/.bin/webpack-dev-server
                        --context ${project.projectDir}
                        --config ${config.get().devOutputFile.get()}
                        build/kotlin-js-min/main/${project.name}.js"""
                .split("\n")
                .flatMap { it.trim().split(" ") }

        logger.lifecycle(commandLine.joinToString(" "))
        super.exec()
    }
}