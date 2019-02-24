# Overview

Plugin that make it easy to get started with kotlin.js and react with gradle

# Set-up

In your `build.gradle.kts`

```
plugins {
    id("io.catacgc.kotlin-react") version "0.1.0"
}
```

# Examples

See the [sample](/sample) dir in this repo for examples of kotlin.js react 
app with tests included. To see any of the examples, just run the dev server:

- `./gradlew -p sample/readme-example build webpackDevServer` - very simple, one file app
- `./gradlew -p sample/kotlin-js-test build webpackDevServer` - multi component app

# Important tasks

- `./gradlew build -t` - compiles kotlin to js 

- `./gradlew webpackDevServer` - start a webpack dev server that auto reloads 
when the output of the kotlin js builds (default: http://localhost:9000)

- `./gradlew webpackTestServer` - start a webpack test server that auto reloads 
when the output of the kotlin js builds (default: http://localhost:9090)

- `./gradlew webpackBuild` - builds a bundle ready for prod (applies kotlin-dce and 
minifies the bundle in `build/bundle`)


# Auto-reload

Auto reload is based on gradle continuous build feature. Just start the webpack web
server in one terminal window (`./gradlew webpackDevServer`) and, in a second window, run `./gradlew build -t`

This will make sure that on any code change, in a few seconds, your dev webserver
window will be updated with latest changes (http://localhost:9000 by default)


# Managing packages

Package management is done via yarn. The plugin sets up an initial set of packages 
(things like webpack-dev-server, react, react-dom, etc) and, from then on,
you manage your own packages via the normal yarn workflow.

In the sample project, `yarn.lock` and `package.json` are intentionally ignored,
but you should commit those in your project.  