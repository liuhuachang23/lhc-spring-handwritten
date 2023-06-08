package com.lhc.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lhc
 * @Date: 2023/6/7 17:46
 * @ClassName: 手写 @Autowired 依赖注入注解
 */
@Retention(RetentionPolicy.RUNTIME) //指示具有注释类型的注释要保留多长时间 （RUNTIME 代表着标记的注解会由JVM保留，因此运行时环境可以使用它。）
@Target(ElementType.FIELD) //指明注解可以修饰的范围 （ FIELD: 用在字段声明（包括枚举） ）
public @interface LhcAutowired {

}
