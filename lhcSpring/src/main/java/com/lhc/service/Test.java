package com.lhc.service;

import com.lhc.config.AppConfig;
import com.lhc.spring.LhcApplicationContext;

/**
 * @Author: lhc
 * @Date: 2023/6/7 16:04
 * @ClassName:
 */
public class Test {

    public static void main(String[] args) {

        LhcApplicationContext applicationContext = new LhcApplicationContext(AppConfig.class);
//        System.out.println(applicationContext.getBean("userService"));
//        System.out.println(applicationContext.getBean("userService"));
//        System.out.println(applicationContext.getBean("userService"));
//        System.out.println(applicationContext.getBean("orderService"));
        UserServiceInterface userServiceInterface = (UserServiceInterface) applicationContext.getBean("userService");
        userServiceInterface.Test();
    }

}
