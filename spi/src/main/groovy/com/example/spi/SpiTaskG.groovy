package com.example.spi

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod;
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class SpiTaskG extends DefaultTask {
   public File javaCompileDestination;
    ClassPool pool
    List<CtClass> targetList = new ArrayList<>()

   public String javaCompileRootPath

    SpiTaskG() {
        pool = ClassPool.getDefault();
    }

    void setResourceDir(File javaCompileDestination, Project project) {
        this.javaCompileDestination = javaCompileDestination
        javaCompileRootPath = javaCompileDestination.absolutePath
        pool.appendClassPath(javaCompileDestination.getAbsolutePath())
        pool.appendClassPath(project.android.bootClasspath[0].toString())
    }

    @TaskAction
    void findClass() {
        javaCompileDestination.eachFileRecurse {
            def name = it.getName()
            System.out.println("SpiTaskG >>>" + name)
            if (name == "MainActivity.class") {
                FileInputStream inputStream = new FileInputStream(it)
                CtClass ctClass = pool.makeClass(inputStream)
                if (ctClass.isFrozen()) {
                    ctClass.defrost()
                }
                CtMethod funOnResume = ctClass.getDeclaredMethod("onStop")

                println("方法名 = " + funOnResume)

                String insert = "android.widget.Toast.makeText(this, \"我是动态插入的代码 onStop ~\", android.widget.Toast.LENGTH_SHORT).show();"

                funOnResume.insertBefore(insert)
                ctClass.writeFile(javaCompileRootPath)
                ctClass.detach()

//                CtClass ctClass = pool.getCtClass("com.example.gradlejavademo.MainActivity")
//                if (ctClass.isFrozen()) {
//                    ctClass.defrost()
//                }
//                CtMethod funOnResume = ctClass.getDeclaredMethod("onResume")
//
//                println("方法名 = " + funOnResume)
//
//                String insert = "android.widget.Toast.makeText(this, \"我是动态插入的代码~\", android.widget.Toast.LENGTH_SHORT).show();"
//
//                funOnResume.insertBefore(insert)
//                ctClass.writeFile(path)
//                ctClass.detach()


            }
        }
    }
}