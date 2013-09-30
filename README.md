# gradle-cocoapods-plugin [![Build Status](https://travis-ci.org/x2on/gradle-cocoapods-plugin.png)](https://travis-ci.org/x2on/gradle-cocoapods-plugin)

A Gradle plugin for Cocoapods.

## Basic usage

Add to your build.gradle

```gradle
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'de.felixschulze.gradle:gradle-cocoapods-plugin:0.1-SNAPSHOT'
    }
}

apply plugin: 'cocoapods'
```

## Advanced usage

Add to your build.gradle

```gradle
cocoapods {
    teamCityLog = true
    ignorePackages = ["AFNetworking"]
    failOnFailure = false
}
```

* `teamCityLog`: Add features for [TeamCity](http://www.jetbrains.com/teamcity/)
* `ignorePackages`: Packages which should be ignored if new versions available

## Changelog

[Releases](https://github.com/x2on/gradle-cocoapods-plugin/releases)

## License

gradle-cocoapods-plugin is available under the MIT license. See the LICENSE file for more info.
