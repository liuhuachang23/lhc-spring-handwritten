package com.lhc.spring;

/**
 * @Author: lhc
 * @Date: 2023/6/8 10:49
 * @ClassName: 手写模拟 BeanPostProcessor 机制（在bean初始化前后，做的事情）
 */
public interface LhcBeanPostProcessor {

    //初始化前
    Object postProcessorBeforeInitialization(String beanName, Object bean);
    //初始化后
    Object postProcessorAfterInitialization(String beanName, Object bean);

}
