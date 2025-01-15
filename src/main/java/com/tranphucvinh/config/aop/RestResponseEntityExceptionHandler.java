package com.tranphucvinh.config.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tranphucvinh.config.exception.BusinessException;
import com.tranphucvinh.config.exception.ResourceNotFoundException;
import com.tranphucvinh.config.pagination.PagingException;
import com.tranphucvinh.config.store.FileException;
import com.tranphucvinh.payload.ERROR;

import io.minio.errors.ErrorResponseException;

@Controller
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private String uri = "";

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException exception, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        uri = getRequestUri(request);
        return ResponseEntity.status(status).body(new ERROR(exception, status, uri));
    }

    @ExceptionHandler(value = {PagingException.class})
    protected ResponseEntity<Object> handlePaging(PagingException exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        uri = getRequestUri(request);
        return ResponseEntity.status(status).body(new ERROR(exception, status, uri));
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBussinessException(BusinessException exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        uri = getRequestUri(request);
        return ResponseEntity.status(status).body(new ERROR(exception, status, uri));
    }

    @ExceptionHandler(value = {ErrorResponseException.class})
    protected ResponseEntity<Object> handleMinioResponseException(Exception exception, WebRequest request) {
        uri = getRequestUri(request);
        ErrorResponseException minioException = (ErrorResponseException) exception;
        HttpStatus status = HttpStatus.valueOf(minioException.response().code());
        return ResponseEntity.status(status).body(new ERROR(exception, status, uri));
    }

    @ExceptionHandler(value = {FileException.class})
    protected ResponseEntity<Object> handleMinioException(FileException exception, WebRequest request) {
        uri = getRequestUri(request);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ERROR(exception, status, uri));
    }

    private String getRequestUri(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI().toString();
    }
}
