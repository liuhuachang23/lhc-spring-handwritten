package com.lhc.spring;

/**
 * @Author: lhc
 * @Date: 2023/6/7 23:33
 * @ClassName: 手写 BeanNameAware 回调接口，用于在创建bean的过程中，将beanName属性设置回bean中
 */
public interface LhcBeanNameAware {

    void setBeanName(String beanName);
}
