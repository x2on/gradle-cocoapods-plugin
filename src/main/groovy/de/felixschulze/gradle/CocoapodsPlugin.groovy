/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Felix Schulze
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.felixschulze.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class CocoapodsPlugin implements Plugin<Project> {

    static final String COCOA_PODS_GROUP_NAME = "CocoaPods"

    void apply(Project project) {
        configureDependencies(project)
        applyExtensions(project)
        applyTasks(project)
    }

    void applyExtensions(final Project project) {
        project.extensions.create('cocoapods', CocoapodsPluginExtension, project)
    }

    void applyTasks(final Project project) {
        CocoapodsRepoUpdateTask cocoapodsRepoUpdateTask = project.tasks.create("updateRepo", CocoapodsRepoUpdateTask)
        cocoapodsRepoUpdateTask.group = COCOA_PODS_GROUP_NAME
        cocoapodsRepoUpdateTask.description = "Update spec repo."
        cocoapodsRepoUpdateTask.outputs.upToDateWhen { false }

        CocoapodsCheckTask cocoapodsCheckTask = project.tasks.create("checkUpdate", CocoapodsCheckTask)
        cocoapodsCheckTask.group = COCOA_PODS_GROUP_NAME
        cocoapodsCheckTask.description = "Check for updates."
        cocoapodsCheckTask.outputs.upToDateWhen { false }
        cocoapodsCheckTask.dependsOn { cocoapodsRepoUpdateTask }

        CocoapodsInstallTask cocoapodsInstallTask = project.tasks.create("installPods", CocoapodsInstallTask)
        cocoapodsInstallTask.group = COCOA_PODS_GROUP_NAME
        cocoapodsInstallTask.description = "Install or update project dependencies."
        cocoapodsInstallTask.outputs.upToDateWhen { false }
        cocoapodsInstallTask.dependsOn { cocoapodsRepoUpdateTask }

    }

    void configureDependencies(final Project project) {
        project.repositories {
            mavenCentral()
        }
    }

}