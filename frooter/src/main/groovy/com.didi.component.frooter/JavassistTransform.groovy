package com.didi.component.frooter;

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import javassist.CtClass
import javassist.CtMethod;
import org.gradle.api.Project
import org.gradle.internal.impldep.org.apache.ivy.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javassist.ClassPool;
import javassist.NotFoundException;

/**
 * Author: fushuang
 * Date: 2020/6/16 下午8:45
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
class JavassistTransform extends Transform {

    Project project;

    //初始化类池
    private final static ClassPool pool = ClassPool.getDefault();


    JavassistTransform(Project project) {
        this.project = project;
    }
    public static final String TAG = "fs666 JavassistTransform   ";

    @Override
    String getName() {
        return "JavassitTransform";
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation);
        Collection<TransformInput> inputs = transformInvocation.getInputs();

        System.out.println(TAG + "transform inputs size : " + inputs.size());

        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)

                System.out.println(TAG + String.format("jarInput >>> originPath : %s , originName : %s , outputPath : %s , outPutName %s"
                        ,jarInput.getFile().getAbsolutePath(),jarInput.getName(),dest.getAbsolutePath(),dest.getName()))


                FileUtils.copyFile(jarInput.getFile(), dest)

            }
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {

                File dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                System.out.println(TAG + String.format("DirectoryInputs >>> originPath : %s , originName : %s , outputPath : %s , outPutName %s"
                        ,directoryInput.getFile().getAbsolutePath(),directoryInput.getName(),dest.getAbsolutePath(),dest.getName()))

                transformCode(directoryInput.getFile().getAbsolutePath(), project)

                FileUtils.copyDirectory(directoryInput.getFile(), dest)
            }
        }

    }

    private void transformCode(String path, Project project) {
        try {
            //将当前路径加入类池,不然找不到这个类
            pool.appendClassPath(path);
            //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
            pool.appendClassPath(project.android.bootClasspath[0].toString());
            //引入android.os.Bundle包，因为onCreate方法参数有Bundle
            pool.importPackage("android.os.Bundle");

            File dir = new File(path);
            dir.eachFileRecurse {
                File file ->
                    String filePath = file.absolutePath
                    println(" fs66688 filePath " + filePath)
                    if (file.getName() == "MainActivity.class") {
                        CtClass ctClass = pool.getCtClass("com.example.gradlejavademo.MainActivity")
                        if (ctClass.isFrozen()) {
                            ctClass.defrost()
                        }
                        CtMethod funOnResume = ctClass.getDeclaredMethod("onResume")

                        println("方法名 = " + funOnResume)

                        String insert = "android.widget.Toast.makeText(this, \"我是动态插入的代码~\", android.widget.Toast.LENGTH_SHORT).show();"

                        funOnResume.insertBefore(insert)
                        ctClass.writeFile(path)
                        ctClass.detach()
                    }

            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}
