package io.catacgc.template

import org.intellij.lang.annotations.Language

@Language("js")
fun KARMA_CONF() = """
const webpackConfig = require("./webpack.test");

module.exports = config => {
  const tests = "../kotlin-js-min/test/*_test.js";

  config.set({
    browsers: ["ChromeHeadless"],
    frameworks: ["mocha"],
    logLevel: config.LOG_INFO,
    files: [
      {
        pattern: tests
      },
    ],
    preprocessors: {
      [tests]: ["webpack", "sourcemap"]
    },
    webpack: webpackConfig,
    singleRun: true
  });
};
""".trimIndent()