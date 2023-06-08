package com.lhc.spring;

/**
 * @Author: lhc
 * @Date: 2023/6/8 10:21
 * @ClassName: 手写模拟 Spring 初始化机制
 */
public interface LhcInitializingBean {

    void afterPropertiesSet();
}
