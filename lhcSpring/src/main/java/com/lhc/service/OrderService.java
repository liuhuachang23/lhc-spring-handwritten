package com.lhc.service;

import com.lhc.spring.LhcAutowired;
import com.lhc.spring.LhcBeanNameAware;
import com.lhc.spring.LhcComponent;
import com.lhc.spring.LhcScope;

/**
 * @Author: lhc
 * @Date: 2023/6/7 22:14
 * @ClassName:
 */
@LhcComponent
//@LhcScope("prototype") //多例
public class OrderService implements LhcBeanNameAware {

    private String BeanName;

    @Override
    public void setBeanName(String beanName) {
        this.BeanName = beanName;
    }
}
