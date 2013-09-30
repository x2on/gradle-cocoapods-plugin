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

class CommandLineRunner {

    def static Process createCommand(String directory, List<String> commandList, Map<String, String> environment) {

        println "Run command: " + commandListToString(commandList)

        if (environment != null) {
            println "with additional environment variables: " + environment
        }
        def processBuilder = new ProcessBuilder(commandList)
        processBuilder.redirectErrorStream(true)
        processBuilder.directory(new File(directory))
        if (environment != null) {
            Map<String, String> env = processBuilder.environment()
            env.putAll(environment)
        }
        def process = processBuilder.start()
        return process
    }


    private static def commandListToString(List<String> commandList) {
        def result = ""
        commandList.each {
            item -> result += item + " "
        }
        return "'" + result.trim() + "'"
    }
}
