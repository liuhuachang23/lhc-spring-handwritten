package com.lhc.spring;

/**
 * @Author: lhc
 * @Date: 2023/6/7 17:47
 * @ClassName: 手写一个 BeanDefinition（Bean的定义）
 */
public class LhcBeanDefinition {

    private Class type; //bean类型
    private String scope; //bean的作用域

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
