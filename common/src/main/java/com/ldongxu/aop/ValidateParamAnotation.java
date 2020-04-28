package com.ldongxu.aop;

import java.lang.annotation.*;

/**
 * 方法或类上添加该注解，则处理该方法或者该类下的所有方法参数校验。
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateParamAnotation {
}
