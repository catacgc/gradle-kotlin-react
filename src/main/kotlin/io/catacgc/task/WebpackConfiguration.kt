package io.catacgc.task

import io.catacgc.template.WEBPACK_DEV
import io.catacgc.template.WEBPACK_PROD
import io.catacgc.template.WEBPACK_TEST
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

open class WebpackConfiguration : DefaultTask() {

    @OutputFile
    var prodOutputFile = project.layout.buildDirectory.file("webpack-config/webpack.prod.js")

    @OutputFile
    var devOutputFile = project.layout.buildDirectory.file("webpack-config/webpack.dev.js")

    @OutputFile
    var testOutputFile = project.layout.buildDirectory.file("webpack-config/webpack.test.js")

    @TaskAction
    fun execute() {
        prodOutputFile.get().asFile.writeText(WEBPACK_PROD())
        devOutputFile.get().asFile.writeText(WEBPACK_DEV())
        testOutputFile.get().asFile.writeText(WEBPACK_TEST())
    }
}