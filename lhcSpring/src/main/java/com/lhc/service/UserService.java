package com.lhc.service;

import com.lhc.spring.*;

/**
 * @Author: lhc
 * @Date: 2023/6/7 16:04
 * @ClassName:
 */
@LhcComponent("userService")
//@LhcScope("prototype") //多例
public class UserService implements LhcBeanNameAware, LhcInitializingBean, UserServiceInterface {

    @LhcAutowired
    private OrderService orderService;

    private String BeanName;

    @Override
    public void setBeanName(String beanName) {
        this.BeanName = beanName;
    }

    @Override
    public void Test() {
        System.out.println("执行Test() 打印：" + orderService);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("调用afterPropertiesSet()");
    }
}
