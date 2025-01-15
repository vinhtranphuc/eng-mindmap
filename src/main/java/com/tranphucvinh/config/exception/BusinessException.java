package com.tranphucvinh.config.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tranphucvinh.constant.ActionEnum;
import com.tranphucvinh.constant.MessageCodeEnum;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 4579933657199062443L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(MessageCodeEnum messageCode) {
        super(messageCode);
    }

    public BusinessException(MessageCodeEnum messageCode, Map<String, Object> data) {
        super(messageCode, data);
    }

    public BusinessException(MessageCodeEnum messageCode, Object... msgData) {
        super(messageCode, msgData);
    }

    public BusinessException(MessageCodeEnum msgCodeEnum, ActionEnum action) {
        super(msgCodeEnum, action);
    }

    public BusinessException(MessageCodeEnum msgCodeEnum, ActionEnum action, Object... msgData) {
        super(msgCodeEnum, action, msgData);
    }
}
