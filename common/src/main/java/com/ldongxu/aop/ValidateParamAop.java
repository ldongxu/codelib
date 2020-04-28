package com.ldongxu.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 使用aop进行方法参数和返回值校验
 *
 * @author liudongxu06
 */
@Component
@Aspect
@Slf4j
public class ValidateParamAop {

    private final Validator validator;
    //获取方法的参数的名称，一般的API无法获取，需要通过解析字节码获取，通常ASM，spring也是使用这个进行获取。
    private final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer;

    public ValidateParamAop() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    @Pointcut("@within(com.ldongxu.aop.ValidateParamAnotation)||@annotation(com.ldongxu.aop.ValidateParamAnotation)")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(JoinPoint point) {
        //  获得切入目标对象
        Object target = point.getThis();
        // 获得切入方法参数
        Object[] args = point.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // 执行校验，获得校验结果
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);
        validateConstraintViolationThrowExpection(validResult,method);
    }

    //进行返回值的校验
    private <T> Set<ConstraintViolation<T>> validReturnValue(T obj, Method method, Object returnValue) {
        ExecutableValidator validatorParam = validator.forExecutables();
        return validatorParam.validateReturnValue(obj, method, returnValue);
    }

    //进行参数校验
    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        ExecutableValidator validatorParam = validator.forExecutables();
        return validatorParam.validateParameters(obj, method, params);
    }

    public void validateConstraintViolationThrowExpection(Set<ConstraintViolation<Object>> validResult, Method method) {
        if (!validResult.isEmpty()) {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            log.info(parameterNames.toString());
            List<FieldError> errors = new ArrayList<FieldError>();
            for (ConstraintViolation item : validResult) {
                //获取参数路径的信息(参数的位置，参数的名称等等)
                PathImpl path = (PathImpl) item.getPropertyPath();
                int paramIndex = path.getLeafNode().getParameterIndex();
                String parameterName = parameterNames[paramIndex];
                FieldError fieldError = new FieldError("", parameterName, item.getMessage());
                errors.add(fieldError);
            }
            String msg = errors.stream().map(e->e.getField()+e.getDefaultMessage()).collect(Collectors.joining(","));

            throw new ParamValidException(msg,errors);
        }
    }

}
