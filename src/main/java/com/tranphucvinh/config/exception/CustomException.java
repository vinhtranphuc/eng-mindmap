package com.tranphucvinh.config.exception;

import java.util.Map;
import java.util.Objects;

import com.tranphucvinh.constant.ActionEnum;
import com.tranphucvinh.constant.MessageCodeEnum;

import lombok.Getter;
import lombok.Setter;

public class CustomException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 6888358176381303221L;

    @Getter
    @Setter
    private String messageCode = "";

    @Getter
    @Setter
    private String action = "";

    @Getter
    private Map<String, Object> data;

    protected CustomException(String message) {
        super(message);
    }

    protected CustomException(MessageCodeEnum msgCodeEnum) {
        super(msgCodeEnum.getMessage());
        this.messageCode = msgCodeEnum.name();
    }

    protected CustomException(MessageCodeEnum msgCodeEnum, Map<String, Object> data) {
        super(msgCodeEnum.getMessage());
        this.messageCode = msgCodeEnum.name();
        this.data = data;
    }

    protected CustomException(MessageCodeEnum msgCodeEnum, Object... msgData) {
        super((Objects.nonNull(msgData) && msgData.length > 0) ? String.format(msgCodeEnum.getMessage(), msgData)
                : msgCodeEnum.getMessage());
        this.messageCode = msgCodeEnum.name();
    }

    protected CustomException(MessageCodeEnum msgCodeEnum, ActionEnum action) {
        super(msgCodeEnum.getMessage());
        this.messageCode = msgCodeEnum.name();
        this.action = action.name();
    }

    protected CustomException(MessageCodeEnum msgCodeEnum, ActionEnum action, Object... msgData) {
        super((Objects.nonNull(msgData) && msgData.length > 0) ? String.format(msgCodeEnum.getMessage(), msgData)
                : msgCodeEnum.getMessage());
        this.messageCode = msgCodeEnum.name();
        this.action = action.name();
    }
}
