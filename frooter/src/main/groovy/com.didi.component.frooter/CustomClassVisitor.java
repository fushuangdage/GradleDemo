package com.didi.component.frooter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.Arrays;

public class CustomClassVisitor extends ClassVisitor {
    private String mClassName;
    private String[] mInterfaces;

    public CustomClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClassName = name;
        mInterfaces = interfaces;
        System.out.println("mInterfaces: " + Arrays.toString(mInterfaces));

    }

    @Override
    public MethodVisitor visitMethod(int access, final String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (isMatchMethod(name)) {
            System.out.println("实现了该接口的类： " + name);
            methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode);
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn(String.format("visit method : %s", name));
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                }
            };
        }
        return methodVisitor;
    }

    private boolean isMatchMethod(String methodName) {
        return methodName.equals("onPause");
    }
}
