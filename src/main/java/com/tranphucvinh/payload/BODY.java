package com.tranphucvinh.payload;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import com.tranphucvinh.config.exception.BusinessException;
import com.tranphucvinh.config.exception.ResourceNotFoundException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BODY {

    private long timestamp;
    private int status;
    private Object data;
    private String message;

    public BODY(int status, Object data, String message) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public BODY(int status, Object data) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.data = data;
    }

    public BODY(Object data, String message) {
        this.timestamp = System.currentTimeMillis();
        this.status = HttpStatus.OK.value();
        this.data = data;
        this.message = message;
    }

    public BODY(Object data) {
        this.timestamp = System.currentTimeMillis();
        this.status = HttpStatus.OK.value();
        this.data = data;
        this.message = "successfully";
    }

    public BODY(ResourceNotFoundException exception) {
        this.timestamp = System.currentTimeMillis();
        this.status = HttpStatus.NOT_FOUND.value();
        this.message = exception.getMessage();
    }

    public BODY(BusinessException exception) {
        this.timestamp = System.currentTimeMillis();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = exception.getMessage();
        this.data = exception.getData();
    }

    public BODY(BadCredentialsException exception) {
        this.timestamp = System.currentTimeMillis();
        this.status = HttpStatus.UNAUTHORIZED.value();
        this.message = "Incorrect username or password";
    }
}
