package com.tranphucvinh.config.exception;

import com.tranphucvinh.constant.MessageCodeEnum;

public class LoginException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 587410656949345271L;

    public LoginException() {
        super(MessageCodeEnum.LOGIN_001);
    }
}
