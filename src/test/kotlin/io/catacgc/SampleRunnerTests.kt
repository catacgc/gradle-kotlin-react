package io.catacgc

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test

class SampleRunnerTests {

    @Test fun webpackBuild() {
        val result = GradleRunner.create()
                .withProjectDir(File("sample/readme-example"))
                .withArguments("clean", "webpackBuild")
                .build()

        println(result.output)
    }
}
