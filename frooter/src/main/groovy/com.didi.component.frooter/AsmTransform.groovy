package com.didi.component.frooter

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.objectweb.asm.ClassWriter
import javassist.ClassPool
import javassist.NotFoundException
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor

/**
 * Author: fushuang
 * Date: 2020/6/16 下午8:45
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
class AsmTransform extends Transform {

    Project project;

    //初始化类池
    private final static ClassPool pool = ClassPool.getDefault();


    AsmTransform(Project project) {
        this.project = project
    }
    public static final String TAG = "fs666  AsmTransform   "

    @Override
    String getName() {
        return "ASMTransform";
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation);
        Collection<TransformInput> inputs = transformInvocation.getInputs();

        System.out.println(TAG + "transform inputs size : " + inputs.size());

        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                System.out.println(TAG + String.format("jarInput >>> originPath : %s , originName : %s , outputPath : %s , outPutName %s",
                        jarInput.getFile().getAbsolutePath(), jarInput.getName(), dest.getAbsolutePath(), dest.getName()))

                FileUtils.copyFile(jarInput.getFile(), dest)

            }
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {

                File dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                System.out.println(TAG + String.format("DirectoryInputs >>> originPath : %s , originName : %s , outputPath : %s , outPutName %s"
                        , directoryInput.getFile().getAbsolutePath(), directoryInput.getName(), dest.getAbsolutePath(), dest.getName()))

                transformCode(directoryInput.getFile().getAbsolutePath(), project);

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

                        byte[] sourceClassBytes = IOUtils.toByteArray(new FileInputStream(file))
                        byte[] resultClassBytes = modifyClass(sourceClassBytes)
                        FileOutputStream fileOutputStream =  new FileOutputStream(file)
                        fileOutputStream.write(resultClassBytes)
                        fileOutputStream.flush()
                    }

            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    public byte[] modifyClass(byte[] srcByteCode) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        ClassVisitor classVisitor=new CustomClassVisitor(classWriter)
        ClassReader classReader = new ClassReader(srcByteCode)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        return classWriter.toByteArray()
    }
}
