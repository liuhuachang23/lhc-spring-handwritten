package com.lhc.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lhc
 * @Date: 2023/6/7 16:11
 * @ClassName: 手写一个 @ComponentScan 包扫描注解
 */
@Retention(RetentionPolicy.RUNTIME) //指示具有注释类型的注释要保留多长时间 （RUNTIME 代表着标记的注解会由JVM保留，因此运行时环境可以使用它。）
@Target(ElementType.TYPE) //指明注解可以修饰的范围 （ TYPE:用在描述类、接口（包括注解类型）或枚举 ）
public @interface LhcComponentScan {

    String value() default ""; //value属性用于指定扫描路径
}
