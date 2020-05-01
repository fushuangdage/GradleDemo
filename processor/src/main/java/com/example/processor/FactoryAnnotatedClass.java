package com.example.processor;

import com.example.annotation.InjectFactory;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import sun.nio.cs.ext.MS874;

/**
 * Author: fushuang
 * Date: 2020/5/2 上午11:06
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
public class FactoryAnnotatedClass {

    public final TypeElement classElement;
    public  Class<?> superClass;
    public  String qualifiedSuperClassName;
    public final String id;

    public FactoryAnnotatedClass(TypeElement classElement) {
        this.classElement = classElement;
        InjectFactory annotation = classElement.getAnnotation(InjectFactory.class);
        superClass = annotation.superClass();
        qualifiedSuperClassName = superClass.getCanonicalName();
        id = annotation.id();
    }

    public FactoryAnnotatedClass(Messager messager, TypeElement typeElement) {
        this.classElement = typeElement;
        InjectFactory annotation = classElement.getAnnotation(InjectFactory.class);
        messager.printMessage(Diagnostic.Kind.NOTE,"FactoryAnnotatedClass");

        try {
            superClass = annotation.superClass();
            qualifiedSuperClassName = superClass.getCanonicalName();

        }catch (MirroredTypeException e){
            DeclaredType typeMirror = (DeclaredType) e.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) typeMirror.asElement();
            qualifiedSuperClassName=classTypeElement.getQualifiedName().toString();

        }

        id = annotation.id();
    }
}
