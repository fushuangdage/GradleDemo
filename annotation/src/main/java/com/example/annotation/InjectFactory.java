package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: fushuang
 * Date: 2020/4/30 下午5:35
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface InjectFactory {

    String id();

    Class superClass();
}
