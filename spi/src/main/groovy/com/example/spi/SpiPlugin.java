package com.example.spi;


import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.compile.JavaCompile;

import java.io.File;
import java.util.function.Consumer;

import static android.databinding.tool.util.StringUtils.capitalize;

public class SpiPlugin implements Plugin<Project> {

    public static final String TAG = ">>>>>>>> SpiPlugin";

    @Override
    public void apply(final Project project) {

        System.out.println("SpiPlugin");

        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(final Project project) {

                AppExtension android = project.getExtensions().getByType(AppExtension.class);
                android.getApplicationVariants().forEach(new Consumer<ApplicationVariant>() {
                    @Override
                    public void accept(ApplicationVariant variant) {

                        final File javaCompileDestination = variant.getJavaCompile().getDestinationDir();

                        final SpiTaskG generateTask = project.getTasks().create(("generateServiceRegistry" + capitalize(variant.getName())),SpiTaskG.class,new Action<SpiTaskG>() {
                            @Override
                            public void execute(SpiTaskG task) {
                                System.out.println(TAG + " execute");

                                task.setResourceDir(javaCompileDestination,project);
                                task.doFirst(new Action<Task>() {
                                    @Override
                                    public void execute(Task task) {
                                        System.out.println(TAG + " : doFirst");
                                    }
                                });
                                task.doLast(new Action<Task>() {
                                    @Override
                                    public void execute(Task task) {
                                        System.out.println(TAG + " : doLast");
                                    }
                                });
                            }
                        });

                        variant.getAssembleProvider().configure(new Action<Task>() {
                            @Override
                            public void execute(Task task) {
                                task.dependsOn(generateTask);
                            }
                        });

                        variant.getJavaCompileProvider().configure(new Action<JavaCompile>() {
                            @Override
                            public void execute(JavaCompile javaCompile) {
                                generateTask.mustRunAfter(javaCompile);
                            }
                        });
                    }
                });


//        project.getTasks().register(spiTask);
//        spiTask.mustRunAfter(assemble);
//                assemble.dependsOn(spiTask);
//                spiTask.mustRunAfter(compile);
//        spiTask.dependsOn(assemble);
            }
        });


    }
}
