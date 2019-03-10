package io.catacgc

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SampleRunnerTests {

    @Test fun webpackBuild() {
        val result = GradleRunner.create()
                .withProjectDir(File("sample/readme-example"))
                .withArguments("clean", "karma")
                .build()

        assertTrue(result.output.contains("TOTAL: 1 SUCCESS"))
    }
}
