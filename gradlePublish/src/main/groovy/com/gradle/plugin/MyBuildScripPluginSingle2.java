package com.gradle.plugin;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * Author: fushuang
 * Date: 2020/5/7 下午3:23
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
public class MyBuildScripPluginSingle2 implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        final CustomParam customArgs = project.getExtensions().create("customArgs", CustomParam.class);

        Task task = project.task("myPluginSingle", new Action<Task>() {
            @Override
            public void execute(Task task) {

                System.out.println(">>>>>>>>>> myPluginSingle222 log tag <<<<<<<<<<< args : " + customArgs.extensionArgs);
                task.doLast(new Action<Task>() {
                    @Override
                    public void execute(Task task) {
                        System.out.println(">>>>>>>>>> myPluginSingle222 doLast  args : " + customArgs.extensionArgs);
                    }
                });
            }
        });
    }
}
