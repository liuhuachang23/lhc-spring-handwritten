package com.lhc.service;

import com.lhc.spring.LhcBeanPostProcessor;
import com.lhc.spring.LhcComponent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: lhc
 * @Date: 2023/6/8 11:00
 * @ClassName:
 */
@LhcComponent
public class LhcBeanPostProcessorImpl implements LhcBeanPostProcessor {

    //针对bean的初始化之前，执行某些事情
    @Override
    public Object postProcessorBeforeInitialization(String beanName, Object bean) {
        //...
        if ("userService".equals(beanName)) {
            System.out.println("执行" + beanName + " postProcessorBeforeInitialization()");
        }
        return bean;
    }

    //针对bean的初始化之后，执行某些事情
    @Override
    public Object postProcessorAfterInitialization(String beanName, Object bean) {
        //...
        if ("userService".equals(beanName)) {
            System.out.println("执行" + beanName + " postProcessorAfterInitialization()");

            //获取 bean的代理对象
            Object proxyInstance = Proxy.newProxyInstance(LhcBeanPostProcessorImpl.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //执行 代理逻辑
                    System.out.println("执行切面逻辑");
                    //再执行，目标bean 中的原本方法
                    return method.invoke(bean,args);
                }
            });
            //返回代理对象
           return proxyInstance;
        }
        return bean;
    }
}
