package com.lhc.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: lhc
 * @Date: 2023/6/7 16:06
 * @ClassName:
 */
public class LhcApplicationContext {

    private Class configClass;

    //保存 所定义的 beanDefinition
    private ConcurrentHashMap<String, LhcBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //单例池，保存单例bean
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    //保存 实现了 LHCBeanPostProcessor 的bean
    private ArrayList<LhcBeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    /**
     * 构造器
     *
     * @param configClass 包扫描作用的 配置类
     *                    1、扫描：将包扫描下的bean，都生成对应的 BeanDefinition，保存起来
     *                    1）扫描 bean --> BeanDefinition(保存 beanName scope等信息) --> BeanDefinitionMap
     *                    2）保存 实现了 LHCBeanPostProcessor 的 bean （用于编写 bean的 初始化前后 执行的事情）
     *                    3、创建单例bean，并保存到 singletonObjects 单例池中
     */
    public LhcApplicationContext(Class configClass) {

        this.configClass = configClass;

        //1、扫描
        //存入的配置类是否有 @LhcComponentScan 包扫描注解
        if (configClass.isAnnotationPresent(LhcComponentScan.class)) {
            //获取这个 包扫描注解
            LhcComponentScan componentScanAnnotation = (LhcComponentScan) configClass.getAnnotation(LhcComponentScan.class);
            //包扫描路径 com.lhc.service （包名）
            String path = componentScanAnnotation.value();
            // com/lhc/service （相对路径）
            path = path.replace(".", "/");
            //获取类加载器
            ClassLoader classLoader = LhcApplicationContext.class.getClassLoader();
            //通过类加载器的getResource()来获取绝对路径，该绝对路径指的是编译后对应class文件的路径：
            // E:\ssm\SpringPrinciple\lhcSpring\target\classes\com\lhc\service
            URL resource = classLoader.getResource(path);
            //封装成一个文件
            File file = new File(resource.getFile());
            //判断这个file是不是一个文件夹，如果是就扫描（获取）文件夹里面的文件（筛选出里面的class类文件）
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath(); //获取文件的文件全路径名
                    //判断是否以 .class 后缀结尾，再判断是否是一个bean（@LhcComponent注解修饰）
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class")).replace("\\", ".");
                        // System.out.println(className);
                        try {
                            //先获取Class类对象（传入该类的全类名，eg: com.lhc.service.UserService ）
                            Class<?> clazz = classLoader.loadClass(className);
                            //如果该对象被 @LhcComponent注解修饰，说明是一个bean
                            if (clazz.isAnnotationPresent(LhcComponent.class)) {

                                //将实现了LHCBeanPostProcessor接口的bean添加到 beanPostProcessorList集合中
                                //isAssignableFrom()：判断该clazz类，是否实现了 LHCBeanPostProcessor接口
                                if (LhcBeanPostProcessor.class.isAssignableFrom(clazz)) {
                                    beanPostProcessorList.add((LhcBeanPostProcessor) clazz.newInstance());
                                }

                                //通过 @LhcComponent注解 获取bean的名称
                                LhcComponent lhcComponentAnnotation = clazz.getAnnotation(LhcComponent.class);
                                String beanName = lhcComponentAnnotation.value();
                                //没有指定就默认生成
                                if ("".equals(beanName)) {
                                    //生成规则为 类名首字母小写
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }
                                //生成一个BeanDefinition对象（定义bean的类型、作用域等信息）
                                LhcBeanDefinition beanDefinition = new LhcBeanDefinition();
                                //设置bean的类型
                                beanDefinition.setType(clazz);
                                //设置bean的作用域
                                if (clazz.isAnnotationPresent(LhcScope.class)) {
                                    //获取 @LhcScope注解
                                    LhcScope scopeAnnotation = clazz.getAnnotation(LhcScope.class);
                                    //获取注解的 value属性，设置为 bean的作用域
                                    beanDefinition.setScope(scopeAnnotation.value());
                                } else {
                                    //没有，默认作用域为单例
                                    beanDefinition.setScope("singleton");
                                }
                                //将beanDefinition保存起来
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        //2、创建单例bean
        for (String beanName : beanDefinitionMap.keySet()) {
            LhcBeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            //当该bean的作用域为单例，且单例池中没有该bean时，去创建该bean
            if ("singleton".equals(beanDefinition.getScope()) && null == singletonObjects.get(beanName)) {
                Object bean = createdBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    /**
     * 创建bean
     *
     * @param beanName       beanName
     * @param beanDefinition beanDefinition
     * @return bean 或者 bean的代理对象
     * 1）创建Bean
     * 2）依赖注入
     * 3）Aware回调
     * 4）BeanPostProcessor初始化前
     * 5）bean初始化
     * 6）BeanPostProcessor初始化后
     */
    //创建bean
    private Object createdBean(String beanName, LhcBeanDefinition beanDefinition) {

        try {

            //1、创建Bean
            //获取Class对象
            Class clazz = beanDefinition.getType();
            //根据无参构造器创建bean（后期再优化）
            Object instance = clazz.getConstructor().newInstance();

            //2、依赖注入
            for (Field f : clazz.getDeclaredFields()) {
                //如果是 @LhcAutowired 注解标注的属性，就使用getBean() 从容器中拿到该属性(Bean对象) 再使用反射 将该属性赋值给 instance（Bean对象）
                if (f.isAnnotationPresent(LhcAutowired.class)) {
                    f.setAccessible(true);
                    f.set(instance, getBean(f.getName())); //从容器中拿
                }
            }

            //3、Aware回调
            // 如果bean实现了LhcBeanNameAware接口，则调用 setBeanName()，将beanName赋值
            if (instance instanceof LhcBeanNameAware) {
                ((LhcBeanNameAware) instance).setBeanName(beanName);
            }

            //4、BeanPostProcessor初始化前 执行postProcessorBeforeInitialization()
            for (LhcBeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorBeforeInitialization(beanName, instance);
            }

            //5、InitializingBean初始化
            // 如果bean实现了 LhcInitializingBean接口，则调用 afterPropertiesSet()，做一些初始化工作
            if (instance instanceof LhcInitializingBean) {
                ((LhcInitializingBean) instance).afterPropertiesSet();
            }

            //6、BeanPostProcessor初始化后 执行postProcessorAfterInitialization()  , AOP
            for (LhcBeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorAfterInitialization(beanName, instance);
            }
            return instance;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    //通过bean的名称获取bean
    public Object getBean(String beanName) {
        //根据beanName获取BeanDefinition
        LhcBeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("bean未找到");
        } else {
            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                //单例，从单例池中获取
                Object bean = singletonObjects.get(beanName);
                if (bean == null) {
                    //没有的话先创建，再保存到单例池中
                    bean = createdBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, bean);
                }
                return bean;
            } else {
                //多例，直接创建
                return createdBean(beanName, beanDefinition);
            }
        }
    }
}
