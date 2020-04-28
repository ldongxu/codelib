package com.ldongxu.util;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @author liudongxu06
 * @date 2017/9/29
 */
public class ValidationUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (CollectionUtils.isNotEmpty(set)) {
            StringBuilder sb = new StringBuilder(obj.getClass().getName() + "参数验证错误->\n");
            for (ConstraintViolation<T> c : set) {
                PathImpl path = (PathImpl) c.getPropertyPath();
                sb.append(path.asString()).append(":");
                sb.append(c.getMessage());
                sb.append("\n");
            }
            System.out.println(sb.toString());
            throw new ValidationException(sb.toString());
        }
    }

    private ValidationUtil() {
    }
}
