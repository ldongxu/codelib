package com.ldongxu.aop;

import org.springframework.validation.FieldError;

import java.util.List;

public class ParamValidException extends RuntimeException {
    private List<FieldError> fieldErrors;

    public ParamValidException(List<FieldError> errors) {
        this.fieldErrors = errors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public ParamValidException(String message, List<FieldError> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }
}
