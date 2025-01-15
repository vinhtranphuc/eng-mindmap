package com.tranphucvinh.config.store;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public FileException(String message) {
        super(message);
        this.message = message;
    }
}
