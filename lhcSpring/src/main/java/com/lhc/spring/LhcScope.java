package com.lhc.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lhc
 * @Date: 2023/6/7 17:46
 * @ClassName: 手写模拟 @Scope 注解 指定bean的作用域
 */
@Retention(RetentionPolicy.RUNTIME) //指示具有注释类型的注释要保留多长时间 （RUNTIME 代表着标记的注解会由JVM保留，因此运行时环境可以使用它。）
@Target(ElementType.TYPE) //指明注解可以修饰的范围 （ TYPE:用在描述类、接口（包括注解类型）或枚举 ）
public @interface LhcScope {

    /**
     * singleton        将单个 bean 定义限定为每个 Spring IoC 容器的单个对象实例（单例）
     * prototype        将单个 bean 定义限定为任意数量的对象实例 （多例）
     * request          将单个 bean 定义限定为单个 HTTP 请求的生命周期 （仅在web类型的 ApplicationContext有效。）
     * session          将单个 bean 定义限定为 HTTP 会话的生命周期 （仅在web类型的 ApplicationContext有效。）
     * application      将单个 bean 定义限定为 ServletContext 的生命周期 （仅在web类型的 ApplicationContext有效。）
     * websocket        将单个 bean 定义限定为 WebSocket 的生命周期 （仅在web类型的 ApplicationContext有效。）
     */
    String value() default "";
}
