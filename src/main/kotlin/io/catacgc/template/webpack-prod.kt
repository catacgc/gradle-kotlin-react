package io.catacgc.template

import org.intellij.lang.annotations.Language

@Language("js")
fun WEBPACK_PROD() = """
    'use strict';

    var path = require('path');

    var config = {
        mode: "production",
        devtool: "cheap-module-eval-source-map",
        output: {
            "filename": "[name].bundle.js",
            "chunkFilename": "[id].bundle.js",
            "publicPath": "/"
        },
        module: {
            "rules": [
                {
                    test: /\.css${'$'}/,
                    use: [
                        {loader: "style-loader"},
                        {loader: "css-loader"}
                    ]
                },
                {
                    test: /\.(png|svg|jpg|gif)${'$'}/,
                    use: [
                        'file-loader'
                    ]
                },
                {
                    test: /\.js${'$'}/,
                    use: ['source-map-loader'],
                    enforce: 'pre'
                }
            ]
        },
        resolve: {
            "alias": {
                "src": path.resolve("src")
            },
            "modules": [
                "build/kotlin-js-min/main",
                "build/resources/main",
                "node_modules"
            ]
        }
    };

    module.exports = config;


""".trimIndent()