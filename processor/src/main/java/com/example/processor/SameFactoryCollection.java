package com.example.processor;

import java.util.HashSet;

/**
 * Author: fushuang
 * Date: 2020/5/2 下午1:46
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
public class SameFactoryCollection {


    public final String qualifiedSuperClassName;
    HashSet<FactoryAnnotatedClass> set=new HashSet<>();

    public SameFactoryCollection(String qualifiedSuperClassName) {
        this.qualifiedSuperClassName = qualifiedSuperClassName;
    }

    public void addAnnotationClass(FactoryAnnotatedClass annotatedClass){
        set.add(annotatedClass);
    }

}
