# gradle-cocoapods-plugin

A Gradle plugin for Cocoapods.

## Basic usage

Add to your build.gradle

```gradle
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.autoscout24.gradle:gradle-cocoapods-plugin:1.3'
    }
}

apply plugin: 'com.autoscout24.gradle.cocoapods'
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
