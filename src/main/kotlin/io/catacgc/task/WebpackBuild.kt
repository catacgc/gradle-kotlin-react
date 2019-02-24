package io.catacgc.task

import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskProvider
import javax.inject.Inject

open class WebpackBuild @Inject constructor(private val config: TaskProvider<WebpackConfiguration>) : Exec() {

    init {
        group = "webpack"
        description = "build the prod artifact"

        inputs.files("src")
        inputs.files("build/kotlin-js-min")
        outputs.dir("build/bundle")
        workingDir = project.projectDir
        dependsOn(config, "yarnInstall", "build")
    }

    override fun exec() {
        commandLine = """node_modules/.bin/webpack
                        --context ${project.projectDir}
                        --config ${config.get().prodOutputFile.get()}
                        --output-path build/bundle
                        --mode production
                        build/kotlin-js-min/main/${project.name}.js"""
                .split("\n")
                .flatMap { it.trim().split(" ") }

        logger.lifecycle(commandLine.joinToString(" "))
        super.exec()
    }
}