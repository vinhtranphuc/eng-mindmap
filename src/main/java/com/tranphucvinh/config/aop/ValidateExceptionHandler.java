package com.tranphucvinh.config.aop;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.tranphucvinh.config.util.Utils;
import com.tranphucvinh.payload.ERROR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ValidateExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public Error handleBindException(BindException ex) {
        BindingResult result = ex.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    protected Error handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<org.springframework.validation.FieldError> errors =
                ex.getConstraintViolations()
                        .stream()
                        .map(constraintViolation -> {
                            String objName = constraintViolation.getRootBeanClass().getName();
                            PathImpl path = (PathImpl) constraintViolation.getPropertyPath();
                            String fieldName = path.toString();
                            String message = constraintViolation.getMessage();
                            return new org.springframework.validation.FieldError(objName, fieldName, message);
                        })
                        .collect(Collectors.toList());
        return processFieldErrors(errors);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(value = {HttpMessageNotReadableException .class})
    protected ResponseEntity<Object> handlePaging(HttpMessageNotReadableException  exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String uri = Utils.getRequestUri();
        return ResponseEntity.status(status).body(new ERROR(exception, status, uri));
    }

    private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        Error error = new Error(BAD_REQUEST.value(), "validation error");
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }

    @Data
    static class Error {
        private long timestamp;
        private final int status;
        private final String error;
        private List<FieldError> fieldErrors = new ArrayList<>();
        private final String path;

        Error(int status, String message) {
            this.timestamp = System.currentTimeMillis();
            this.status = status;
            this.error = message;
            this.path = Utils.getRequestUri();
        }

        public void addFieldError(String fieldName, String message) {
            FieldError error = new FieldError(fieldName, message);
            fieldErrors.add(error);
        }

        public List<FieldError> getFieldErrors() {
            return fieldErrors;
        }

        @AllArgsConstructor
        @Getter
        class FieldError {
            private String fieldName;
            private String message;
        }
    }
}
