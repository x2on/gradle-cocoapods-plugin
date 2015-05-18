/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013-2014 Felix Schulze
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

import org.apache.commons.lang.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import de.felixschulze.teamcity.TeamCityStatusMessageHelper
import de.felixschulze.teamcity.TeamCityStatusType

class CocoapodsCheckTask extends DefaultTask {

    private static final Logger LOG = LoggerFactory.getLogger(CocoapodsCheckTask.class)


    @TaskAction
    def checkUpdates() throws IOException {

        CocoapodsPluginExtension cocoaPodsPluginExtension = project.cocoapods

        def commands = [
                "pod",
                "outdated",
                "--no-repo-update"
        ]

        Process process = CommandLineRunner.createCommand(".", commands, null)

        Boolean updateAvailable = false

        def ArrayList<String> packageNamesWithUpdates = new ArrayList<String>()

        process.inputStream.eachLine {
            if (updateAvailable && it.startsWith("- ") && it.contains(" -> ")) {
                //Cleanup package name ("- AFNetworking 1.3.2 -> 1.3.2 (latest version 2.0.0)" --> "AFNetworking")
                String packageName = it.minus("- ").minus(~/ \d((\d*)\.?).*? -> .*/)
                Collection<String> ignorePackages = cocoaPodsPluginExtension.ignorePackages
                if (ignorePackages!= null && !ignorePackages.isEmpty() && ignorePackages.contains(packageName)) {
                    LOG.info("Package " + packageName + " ignored.")
                } else {
                    LOG.warn("Update available for " + packageName + ".")
                    packageNamesWithUpdates.add(packageName)
                }
            }
            if (it.contains("The following updates are available:") || it.contains("The following pod updates are available:")) {
                updateAvailable = true
            }
            LOG.debug(it)
        }

        process.waitFor()

        if (!packageNamesWithUpdates.empty) {

            def String message = "${packageNamesWithUpdates.size()} ${packageNamesWithUpdates.size() > 1 ? "Updates" : "Update" } available (${StringUtils.join(packageNamesWithUpdates,", ")})"

            if (cocoaPodsPluginExtension.teamCityLog) {
                println TeamCityStatusMessageHelper.buildStatusString(TeamCityStatusType.FAILURE, message)
            }
            throw new GradleScriptException(message, null)
        } else {
            if (cocoaPodsPluginExtension.teamCityLog) {
                println TeamCityStatusMessageHelper.buildStatusString(TeamCityStatusType.NORMAL, "No updates available")
            }

        }
    }
}
