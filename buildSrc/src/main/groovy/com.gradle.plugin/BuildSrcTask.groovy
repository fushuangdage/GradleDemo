package com.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class BuildSrcTask extends DefaultTask {
    String message = "BuildSrcTask default message"

    @TaskAction
    //标识Task要执行的动作
    def hello() {
        println message
    }
}


