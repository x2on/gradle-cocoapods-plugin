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

package com.autoscout24.gradle

import de.felixschulze.teamcity.TeamCityStatusMessageHelper
import de.felixschulze.teamcity.TeamCityStatusType
import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CocoapodsRepoUpdateTask extends DefaultTask {

    private static final Logger LOG = LoggerFactory.getLogger(CocoapodsRepoUpdateTask.class)

    @TaskAction
    def checkUpdates() throws IOException {

        def commands = [
                "pod",
                "repo",
                "update"
        ]

        Process process = CommandLineRunner.createCommand(".", commands, null)

        process.inputStream.eachLine {
            LOG.info(it)
        }

        process.waitFor()

        if (process.exitValue() > 0) {
            if (project.cocoapods.teamCityLog) {
                println TeamCityStatusMessageHelper.buildStatusString(TeamCityStatusType.FAILURE, "CocoaPods: Update repo failed")
            }
            throw new GradleScriptException("CocoaPods: Update repo failed", null)
        }
    }
}
