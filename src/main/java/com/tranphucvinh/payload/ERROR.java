package com.tranphucvinh.payload;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.tranphucvinh.config.exception.CustomException;
import com.tranphucvinh.config.util.Utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ERROR {

    private long timestamp;
    private int status;
    private String message;
    private String messageCode;
    private String action;
    private String path;
    private Map<String, Object> errorData;

    public ERROR(Exception exception, HttpStatus httpStatus) {
        this.timestamp = System.currentTimeMillis();
        this.status = httpStatus.value();
        this.message = exception.getMessage();
        this.path = Utils.getRequestUri();
    }

    public ERROR(Exception exception, HttpStatus httpStatus, String messageCode) {
        this.timestamp = System.currentTimeMillis();
        this.status = httpStatus.value();
        this.message = exception.getMessage();
        this.messageCode = messageCode;
        this.path = Utils.getRequestUri();
    }

    public ERROR(CustomException exception, HttpStatus httpStatus) {
        this.timestamp = System.currentTimeMillis();
        this.status = httpStatus.value();
        this.message = exception.getMessage();
        this.messageCode = exception.getMessageCode();
        this.action = exception.getAction();
        this.path = Utils.getRequestUri();
        this.errorData = exception.getData();
    }
}
