package io.catacgc.template

import org.intellij.lang.annotations.Language

@Language("js")
fun WEBPACK_DEV() = """
    'use strict';

    var ErrorOverlayPlugin = require('error-overlay-webpack-plugin');
    var path = require('path');


    var config = {
        mode: "development",
        plugins: [new ErrorOverlayPlugin()],
        devtool: 'cheap-module-source-map',
        devServer: {
            // this is where index.html is loaded from
            contentBase: 'build/resources/main',
            compress: true,
            port: 9000
        },
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
                "build/resources/test",
                "node_modules"
            ]
        }
    };

    module.exports = config;


""".trimIndent()