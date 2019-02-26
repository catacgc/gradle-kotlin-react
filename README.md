# Overview

Plugin that make it easy to get started with kotlin.js and react with gradle

# Set-up

In your `build.gradle.kts`

```
plugins {
    id("io.catacgc.kotlin-react") version "<latest_version>"
}
```

# Examples

See the [sample](/sample) dir in this repo for examples of kotlin.js react 
app with tests included. To see any of the examples, just run the dev server:

- `./gradlew -p sample/readme-example build webpackDevServer` - very simple, one file app
- `./gradlew -p sample/sum-ui-example build webpackDevServer` - multi component app

# Important tasks

- `./gradlew build -t` - compiles kotlin to js 

- `./gradlew webpackDevServer` - start a webpack dev server that auto reloads 
when the output of the kotlin js builds (default: http://localhost:9000)

- `./gradlew webpackTestServer` - start a mocha test server that auto reloads 
when the output of the kotlin js builds (default: http://localhost:9090)

- `./gradlew karma` - headless testing with Chrome 

- `./gradlew webpackBuild` - builds a bundle ready for prod (applies kotlin-dce and 
minifies the bundle in `build/bundle`)


# Auto-reload

Auto reload is based on the Gradle continuous build feature. Just run the build continuously `./gradlew build -t`.
This will compile Kotlin sources anytime you touch your main / test source sets.

In a separate terminal window start the web server: `./gradle webpackDevServer` or 
the test server `./gradle webpackTestServer` and you have the perfect dev environment that auto-refreshes 
the browser anytime when you save your file.   


#### Auto-reload performance!

Gradle's continuous build feature, while functionally working, is somehow slow on OSX. This is because of 
both the Java WatchService implementation and some gradle changes (thumbs up on this [issue](https://github.com/gradle/gradle/issues/3427))

I've had better mileage setting up a poor's man continuous build using [fswatch](https://github.com/emcrisostomo/fswatch)


```
# One terminal window (instead of ./gradlew build -t)

fswatch -0 src | xargs -0 -I'{}' ./gradlew  build

# And the other

./gradle webpackDevServer
```

# Managing packages

Package management is done via yarn. The plugin sets up an initial set of packages 
(things like webpack-dev-server, react, react-dom, etc) and, from then on,
you manage your own packages via the normal yarn workflow.

In the sample project, `yarn.lock` and `package.json` are intentionally ignored,
but you should commit those in your project.  