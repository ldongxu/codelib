//package com.ldongxu;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
///**
// * 获取spring bean工具类
// *
// * Created by 刘东旭 on 2017/7/24.
// */
//@Component
//public class SpringUtil implements ApplicationContextAware {
//    private static ApplicationContext applicationContext = null;
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        if (SpringUtil.applicationContext==null){
//            SpringUtil.applicationContext = applicationContext;
//        }
//    }
//
//    public static ApplicationContext getApplicationContext(){
//        return applicationContext;
//    }
//
//    public static Object getBean(String name){
//        return applicationContext.getBean(name);
//    }
//
////    public static <T extends Object> T getBean(String name,Class<T> tClass){
////        return (T)applicationContext.getBean(name);
////    }
//}
