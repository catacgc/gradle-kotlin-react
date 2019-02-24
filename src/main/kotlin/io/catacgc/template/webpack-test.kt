package io.catacgc.template

import org.intellij.lang.annotations.Language

@Language("js")
fun WEBPACK_TEST() = """
'use strict';

const path = require('path');
const WebpackShellPlugin = require('webpack-shell-plugin');
const ErrorOverlayPlugin = require('error-overlay-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');


var config = {
    mode: "development",
    devtool: 'source-map',
    plugins: [
        new ErrorOverlayPlugin(),
        new HtmlWebpackPlugin()
    ],
    devServer: {
        hot: true,
        overlay: true,
        compress: true,
        port: 9090
    },
    output: {
        filename: 'test.js',
        devtoolModuleFilenameTemplate: '[absolute-resource-path]',
        devtoolFallbackModuleFilenameTemplate: '[absolute-resource-path]?[hash]'
    },
    module: {
        "rules": [
            {
                test: /\.css${'$'}/,
                use: [
                    {loader: "null-loader"}
                ]
            },
            {
                test: /\.(png|svg|jpg|gif|ico)${'$'}/,
                use: [
                    'null-loader'
                ]
            }
        ]
    },
    resolve: {
        "alias": {
            "src": path.resolve("src")
        },
        "modules": [
            "build/kotlin-js-min/test",
            "build/kotlin-js-min/main",
            "build/resources/main",
            "build/resources/test",
            "node_modules"
        ]
    }
};

module.exports = config;
""".trimIndent()